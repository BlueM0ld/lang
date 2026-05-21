package com.study.mandarin.lang.vocab;

import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import com.study.mandarin.lang.vocab.model.VocabItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VocabMapper {

    @Mapping(target = "confidenceScore", expression = "java(confidenceScorePercentage(vocabItem.getConfidenceScore()))")
    VocabItemDTO vocabItemToVocabItemDTO(VocabItem vocabItem);

    default int confidenceScorePercentage(int cScore){
        return (cScore * 100) / 10;
    }
}