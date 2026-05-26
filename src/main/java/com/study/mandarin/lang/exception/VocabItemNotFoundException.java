package com.study.mandarin.lang.exception;

public class VocabItemNotFoundException extends RuntimeException {

    public VocabItemNotFoundException(String id) {
        super("Vocab item not found with id: " + id);
    }
}