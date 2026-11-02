package com.pictet.bookservice.domain.model;

import java.util.Objects;

public record Option(String description, int optionIndex, Integer targetSectionId, Consequence consequence) {

    public Option(String description, int optionIndex, Integer targetSectionId, Consequence consequence) {
        this.description = Objects.requireNonNull(description, "Option description cannot be null");
        this.optionIndex = optionIndex;
        this.targetSectionId = Objects.requireNonNull(targetSectionId, "Target section ID cannot be null");
        this.consequence = consequence;
    }

    public boolean hasConsequence() {
        return consequence != null;
    }
}
