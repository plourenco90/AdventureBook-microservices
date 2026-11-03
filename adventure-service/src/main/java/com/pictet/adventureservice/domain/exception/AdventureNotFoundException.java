package com.pictet.adventureservice.domain.exception;

public class AdventureNotFoundException extends RuntimeException {

    public AdventureNotFoundException(Long id) {
        super("Adventure not found: " + id);
    }
}