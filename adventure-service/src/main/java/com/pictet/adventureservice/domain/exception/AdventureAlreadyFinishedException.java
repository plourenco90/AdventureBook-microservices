package com.pictet.adventureservice.domain.exception;

public class AdventureAlreadyFinishedException extends RuntimeException {

    public AdventureAlreadyFinishedException(Long id) {
        super("Adventure is already finished: " + id);
    }
}