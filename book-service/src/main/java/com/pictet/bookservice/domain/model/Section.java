package com.pictet.bookservice.domain.model;

import java.util.List;
import java.util.Objects;


public record Section(Integer id, String text, SectionType type, List<Option> options) {

    public Section {
        Objects.requireNonNull(id, "Section ID cannot be null");
        Objects.requireNonNull(text, "Section Text cannot be null");
        Objects.requireNonNull(type, "Section Type cannot be null");
        options = options == null ? List.of() : List.copyOf(options);
    }

    public boolean isBeginning() {
        return type == SectionType.BEGIN;
    }

    public boolean isEnding() {
        return type == SectionType.END;
    }
}
