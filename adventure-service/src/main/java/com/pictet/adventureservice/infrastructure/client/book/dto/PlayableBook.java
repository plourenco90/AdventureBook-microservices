package com.pictet.adventureservice.infrastructure.client.book.dto;

import java.util.List;

public record PlayableBook(Long id, String title, String author, List<PlayableSection> sections) {
}