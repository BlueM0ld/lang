package com.study.mandarin.lang.drill.dto;

import com.study.mandarin.lang.vocab.dto.QualityOfRecall;
import com.study.mandarin.lang.vocab.dto.VocabItemDTO;

public record DrillResultRequest(VocabItemDTO vocabItem, QualityOfRecall qualityOfRecall) {
}
