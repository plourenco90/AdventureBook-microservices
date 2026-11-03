package com.pictet.adventureservice.domain.exception;

public class SectionNotFoundException extends RuntimeException {

    public SectionNotFoundException(Integer sectionId) {
        super("The Section does not exist: " + sectionId);
    }
}