package com.study.mandarin.lang.drill;

import com.study.mandarin.lang.drill.dto.DrillOptionDto;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DrillMapper {

    DrillQuestionDto toDrillQuestionDto(DrillOptionDto vocabItem);

    DrillOptionDto toDrillOptionDto(VocabItemDTO vocabItem);

}