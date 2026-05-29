package com.study.mandarin.lang.vocab;

import com.study.mandarin.lang.vocab.dto.AddVocab;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import com.study.mandarin.lang.vocab.model.VocabItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VocabMapper {

    @Mapping(target = "confidenceScore", expression = "java(confidenceScorePercentage(vocabItem.getConfidenceScore()))")
    VocabItemDTO vocabItemToVocabItemDTO(VocabItem vocabItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "confidenceScore", expression = "java(0)")
    @Mapping(target = "nextReviewDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "available", expression = "java(true)")
    @Mapping(target = "streak", expression = "java(0)")
    VocabItem addNewVocab(AddVocab vocab);

    default int confidenceScorePercentage(int cScore){
        return (cScore * 100) / 10;
    }
}