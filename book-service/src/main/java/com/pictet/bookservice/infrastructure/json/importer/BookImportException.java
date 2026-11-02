package com.pictet.bookservice.infrastructure.json.importer;

public class BookImportException extends RuntimeException {

    public BookImportException(String message, Throwable cause) {
        super(message, cause);
    }
}