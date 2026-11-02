package com.pictet.bookservice.domain.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Book(Long id, String title, String author, List<Section> sections) {

    public Book(Long id, String title, String author, List<Section> sections) {
        this.id = Objects.requireNonNull(id, "Book ID cannot be null");
        this.title = Objects.requireNonNull(title, "Book title cannot be null");
        this.author = Objects.requireNonNull(author, "Book author cannot be null");
        this.sections = List.copyOf(Objects.requireNonNull(sections, "Sections cannot be null"));
    }

    public Optional<Section> findBeginningSection() {
        return sections.stream()
                .filter(Section::isBeginning)
                .findFirst();
    }
}
