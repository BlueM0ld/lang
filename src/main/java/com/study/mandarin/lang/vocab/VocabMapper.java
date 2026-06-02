package com.study.mandarin.lang.vocab;

import com.study.mandarin.lang.vocab.dto.AddVocab;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import com.study.mandarin.lang.vocab.model.VocabItem;
import com.study.mandarin.lang.vocab.dto.Memory;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VocabMapper {

    VocabItemDTO vocabItemToVocabItemDTO(VocabItem vocabItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vocabId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "nextReviewDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "available", expression = "java(true)")
    @Mapping(target = "memory", ignore = true)
    VocabItem addNewVocab(AddVocab vocab);

    @AfterMapping
    default void initMemory(@MappingTarget VocabItem item) {
        item.setMemory(Memory.initial());
    }

}