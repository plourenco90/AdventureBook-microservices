package com.pictet.adventureservice.infrastructure.client.book.exception;

public class InvalidBookException extends RuntimeException {

    public InvalidBookException(Long id) {
        super("Book is invalid: " + id);
    }
}
