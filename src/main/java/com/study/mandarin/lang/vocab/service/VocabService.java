package com.study.mandarin.lang.vocab.service;


import com.study.mandarin.lang.drill.dto.DrillType;
import com.study.mandarin.lang.exception.UnknownDrillTypeException;
import com.study.mandarin.lang.exception.VocabItemNotFoundException;
import com.study.mandarin.lang.utils.Tone;
import com.study.mandarin.lang.vocab.persistence.adapter.VocabMapper;
import com.study.mandarin.lang.vocab.persistence.VocabRepository;
import com.study.mandarin.lang.vocab.dto.*;
import com.study.mandarin.lang.vocab.model.VocabItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VocabService {

    private final VocabRepository vocabRepository;
    private final SpacedRepetitionService spacedRepetitionService;
    private final VocabMapper mapper;


    public List<VocabItemDTO> getAllVocab(){
        List<VocabItemDTO> items = vocabRepository
                                        .findAll()
                                        .stream()
                                        .map(mapper::vocabItemToVocabItemDTO).toList();
        log.info("Count: {}", items.size());
        return items;
    }

    public List<VocabItemDTO>  getRandomVocabList(int numberOfVocab){
        return vocabRepository.getRandomSample(numberOfVocab).stream().map(mapper::vocabItemToVocabItemDTO).toList();
    }

    public VocabItem getVocabItem(String id){
        return vocabRepository.findById(id).orElseThrow(()-> new VocabItemNotFoundException(id));
    }

    public String addVocab(AddVocab vocab){

        List<Tone> tones = Tone.extractAll(vocab.pinyin());
        VocabItem newVocabItem = mapper.addNewVocab(vocab, tones);
        var savedItem = vocabRepository.save(newVocabItem);
        return savedItem.getId();
    }

    public String updateVocab(UpdateVocab vocab){
        vocabRepository.updateVocab(vocab);
        return vocab.id();
    }

    public String deleteVocab(String id){
        vocabRepository.deleteById(id);
        return id;
    }

    public String recordDrillResult(String vocabId, DrillType drillType, QualityOfRecall qualityOfRecall) {
        VocabItem item = vocabRepository.findByVocabId(vocabId)
                .orElseThrow(() -> new VocabItemNotFoundException("Vocabulary item not found"));
        VocabMemory vocabMemory= item.getVocabMemory();
        Memory currentMem = getCurrentMemModal(drillType,vocabMemory);
        Memory updatedMemory = spacedRepetitionService.calculateUpdatedMemory(currentMem,qualityOfRecall);

        LocalDate nextReviewDate = spacedRepetitionService.calculateNextReviewDate(updatedMemory, qualityOfRecall);
        vocabRepository.updateVocabMemory(
                vocabId,
                updatedMemory,
                nextReviewDate,
                drillType
        );
        return  item.getId();
    }

    private Memory getCurrentMemModal(DrillType drillType, VocabMemory item) {
        return switch (drillType) {
            case RECOGNITION, READING -> item.getReading();
            case LISTENING, TONE_PAIR, SHADOWING -> item.getListening();
            case SPEAKING -> item.getSpeaking();
            case WRITING -> item.getWriting();
            case FREE_RECALL -> item.getWriting(); // temporary mapping
        };
    }
    public List<VocabItemDTO> getVocab(String search, boolean dueOnly) {
        return vocabRepository.findWithFilters(search,dueOnly).stream().map(
                mapper::vocabItemToVocabItemDTO
        ).toList();
    }
}
