package com.pictet.adventureservice.domain.exception;

import com.pictet.adventureservice.infrastructure.client.book.dto.ConsequenceType;

public class UnsupportedConsequenceException extends RuntimeException {

    public UnsupportedConsequenceException(ConsequenceType type) {
        super("The consequence is not supported: " + type.name());
    }
}