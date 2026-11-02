package com.pictet.bookservice.domain.exception;

public class InvalidBookException extends RuntimeException {

    public InvalidBookException(Long id) {
        super("Book is invalid: " + id);
    }
}
