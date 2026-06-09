package com.study.mandarin.lang.vocab.dto;

import lombok.Data;

@Data
public class VocabMemory {
    private Memory listening;
    private Memory speaking;
    private Memory reading;
    private Memory writing;

    public static VocabMemory initial() {
        VocabMemory memory = new VocabMemory();
        memory.setListening(Memory.initial());
        memory.setSpeaking(Memory.initial());
        memory.setReading(Memory.initial());
        memory.setWriting(Memory.initial());
        return memory;
    }
}

