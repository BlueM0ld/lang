package com.study.mandarin.lang.exception;

public class UnknownDrillTypeException extends RuntimeException {

    public UnknownDrillTypeException(String type) {
        super("Invalid drill type: " + type);
    }
}