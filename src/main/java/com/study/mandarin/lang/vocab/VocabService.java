package com.study.mandarin.lang.vocab;


import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import com.study.mandarin.lang.vocab.model.VocabItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VocabService {

    private final VocabRepository vocabRepository;
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
        return vocabRepository.findById(id).orElseThrow(()-> new RuntimeException("VocabItem doesn't exist"));
    }

    public void updateConfidenceAndRevisionDate(VocabItem vocabItem, boolean success) {
        vocabRepository.updateConfidence(vocabItem,success);
    }
}
