package com.study.mandarin.lang.drill.dto;

public enum DrillType {
    RECOGNITION,  // pick meaning (existing)
    LISTENING,    // hear audio → pick meaning
    TONE_PAIR,    // hear two-char word → identify tone pair
    READING,      // see sentence → pick meaning
    FREE_RECALL,  // see character → type pinyin
    SPEAKING,     // record word → AI tone evaluation
    SHADOWING,    // hear phrase → record → compare
    WRITING       // stroke order practice
}
