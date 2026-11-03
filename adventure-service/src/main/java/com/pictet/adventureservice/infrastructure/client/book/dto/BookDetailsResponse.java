package com.pictet.adventureservice.infrastructure.client.book.dto;

import java.util.List;

public record BookDetailsResponse(Long id, String title, String author, List<SectionResponse> sections) {

    public enum SectionType {
        BEGIN,
        NODE,
        END
    }

    public enum ConsequenceType {
        LOSE_HEALTH
    }

    public record SectionResponse(Integer id, String text, SectionType type, List<OptionResponse> options) {
    }

    public record OptionResponse(String description, int optionIndex, Integer goToId, ConsequenceResponse consequence) {
    }

    public record ConsequenceResponse(ConsequenceType type, Integer value, String text) {
    }
}