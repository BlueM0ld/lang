package com.study.mandarin.lang.drill.fixtures


import com.study.mandarin.lang.drill.dto.*
import com.study.mandarin.lang.drill.dto.options.DrillOption
import com.study.mandarin.lang.drill.dto.options.RecognitionOption
import com.study.mandarin.lang.drill.dto.questions.DrillQuestion
import com.study.mandarin.lang.drill.dto.questions.RecognitionQuestion
import com.study.mandarin.lang.vocab.dto.QualityOfRecall


final class DrillFixtures {

    private DrillFixtures() {}

    static DrillQuestion question() {
        return new RecognitionQuestion(
                "你",
                "nǐ"
        );
    }

    static DrillOption option() {
        return new RecognitionOption(
                "你",
                "nǐ",
                "you"
        );
    }

    static DrillDto drillDto() {
        return DrillDto.builder()
                .vocabQuestion(question())
                .vocabItemOptions(List.of(option()))
                .build();
    }

    static DrillResultRequest request(boolean correct=true) {
        return new DrillResultRequest(
                "vocab-id-123",
                DrillType.READING,
                correct as QualityOfRecall
        );
    }
}