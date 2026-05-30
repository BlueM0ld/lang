package com.study.mandarin.lang.vocab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QualityOfRecall {
    ZERO(0,"No recall", false),
    ONE(1,"Incorrect; answer seemed familiar", false),
    TWO(2,"Incorrect, but remembered after seeing answer", false),
    THREE(3,"Correct, but difficult", true),
    FOUR(4,"Correct, but with hesitation", true),
    FIVE(5,"Perfect recall", true);

    private final int score;
    private final String meaning;
    private final boolean successfulRecall;
}