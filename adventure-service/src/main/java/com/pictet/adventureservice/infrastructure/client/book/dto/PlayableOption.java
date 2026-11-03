package com.pictet.adventureservice.infrastructure.client.book.dto;

public record PlayableOption(
        String description,
        int optionIndex,
        Integer targetSectionId,
        PlayableConsequence consequence
) {
}