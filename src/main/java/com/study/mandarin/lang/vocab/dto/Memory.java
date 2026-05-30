package com.study.mandarin.lang.vocab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Memory {
    int repetitions;
    double easeFactor;
    int lastInterval; //last interval (previous state)

    public static Memory initial(){
        Memory mem = new Memory();
        mem.setRepetitions(0);
        mem.setEaseFactor(2.5);
        mem.setLastInterval(0);
        return mem;
    }
}
