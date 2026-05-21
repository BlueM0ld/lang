package com.study.mandarin.lang.vocab;

import com.study.mandarin.lang.vocab.model.VocabItem;

import java.util.List;

public interface CustomVocabRepository {

    List<VocabItem> getRandomSample(int size);

    void updateConfidence(VocabItem vocabId, boolean success);
}
