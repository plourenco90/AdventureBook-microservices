package com.pictet.bookservice.domain.model;

import java.util.Objects;

public record Consequence(ConsequenceType type, Integer value, String text) {

    public Consequence(ConsequenceType type, Integer value, String text) {
        this.type = Objects.requireNonNull(type, "Consequence Type cannot be null");
        this.value = Objects.requireNonNull(value, "Consequence Value cannot be null");
        this.text = text;

        if (value < 0) {
            throw new IllegalArgumentException("Consequence Value cannot be negative");
        }
    }
}
