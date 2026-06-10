package com.study.mandarin.lang.vocab.persistence.adapter;

import com.study.mandarin.lang.utils.Tone;
import com.study.mandarin.lang.vocab.dto.AddVocab;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;
import com.study.mandarin.lang.vocab.dto.VocabMemory;
import com.study.mandarin.lang.vocab.model.VocabItem;
import com.study.mandarin.lang.vocab.dto.Memory;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VocabMapper {

    VocabItemDTO vocabItemToVocabItemDTO(VocabItem vocabItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vocabId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "nextReviewDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "available", expression = "java(true)")
    @Mapping(target = "vocabMemory", ignore = true)
    @Mapping(target = "toneSequence", source = "tones")
    VocabItem addNewVocab(AddVocab vocab, List<Tone> tones);

    @AfterMapping
    default void initMemory(@MappingTarget VocabItem item) {
        item.setVocabMemory(VocabMemory.initial());
    }

}