package com.pictet.adventureservice.domain.exception;

public class InvalidOptionException extends RuntimeException {

    public InvalidOptionException(Integer index) {
        super("Invalid option index: " + index);
    }
}