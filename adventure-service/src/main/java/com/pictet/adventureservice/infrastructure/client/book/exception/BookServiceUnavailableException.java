package com.pictet.adventureservice.infrastructure.client.book.exception;

public class BookServiceUnavailableException extends RuntimeException {

    public BookServiceUnavailableException(String message) {
        super(message);
    }

    public BookServiceUnavailableException(Exception ex) {
        super(ex);
    }
}
