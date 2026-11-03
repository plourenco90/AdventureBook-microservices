package com.pictet.adventureservice.infrastructure.client.book.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Book not found with id: " + id);
    }
}
