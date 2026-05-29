package com.study.mandarin.lang.vocab.service;


import com.study.mandarin.lang.exception.VocabItemNotFoundException;
import com.study.mandarin.lang.vocab.VocabMapper;
import com.study.mandarin.lang.vocab.VocabRepository;
import com.study.mandarin.lang.vocab.dto.AddVocab;
import com.study.mandarin.lang.vocab.dto.UpdateVocab;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
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

        VocabItem newVocabItem = mapper.addNewVocab(vocab);
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


    public void recordDrillResult(String id, boolean success) {
        VocabItem item = getVocabItem(id);
        LocalDate nextReviewDate = spacedRepetitionService.calculateNextReviewDate(item.getStreak(), success);
        int newStreak = spacedRepetitionService.calculateNewStreak(item.getStreak(), success);
        vocabRepository.updateScheduling(item, nextReviewDate, newStreak);
    }

    public List<VocabItemDTO> getVocab(String search, boolean dueOnly) {
        return vocabRepository.findWithFilters(search,dueOnly).stream().map(
                mapper::vocabItemToVocabItemDTO
        ).toList();
    }
}
