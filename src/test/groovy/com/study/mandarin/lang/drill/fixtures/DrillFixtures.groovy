package com.study.mandarin.lang.drill.fixtures

import com.study.mandarin.lang.drill.DrillQuestionDto;
import com.study.mandarin.lang.drill.dto.*;


public final class DrillFixtures {

    private DrillFixtures() {}

    static DrillQuestionDto question() {
        return new DrillQuestionDto(
                "你",
                "nǐ"
        );
    }

    static DrillOptionDto option() {
        return new DrillOptionDto(
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
                correct
        );
    }
}