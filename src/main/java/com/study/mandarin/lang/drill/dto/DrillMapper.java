package com.study.mandarin.lang.drill.dto;

import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import com.study.mandarin.lang.vocab.model.VocabItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DrillMapper {

    DrillQuestionDto toDrillQuestionDto(DrillOptionDto vocabItem);

    DrillOptionDto toDrillOptionDto(VocabItemDTO vocabItem);

}