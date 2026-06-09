package com.study.mandarin.lang.vocab.persistence;

import com.study.mandarin.lang.vocab.dto.Memory;
import com.study.mandarin.lang.vocab.dto.UpdateVocab;
import com.study.mandarin.lang.vocab.model.VocabItem;

import java.time.LocalDate;
import java.util.List;

public interface CustomVocabRepository {

    List<VocabItem> getRandomSample(int size);

    void updateVocab(UpdateVocab vocab);

    List<VocabItem> findWithFilters(String search, boolean dueOnly);

    void updateVocabMemory(String vocabId, Memory updatedMemory, LocalDate nextReviewDate);
}
