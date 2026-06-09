package com.study.mandarin.lang.drill.dto;

import com.study.mandarin.lang.vocab.dto.QualityOfRecall;

public record DrillResultRequest(String vocabId, DrillType drillType, QualityOfRecall qualityOfRecall) {
}
