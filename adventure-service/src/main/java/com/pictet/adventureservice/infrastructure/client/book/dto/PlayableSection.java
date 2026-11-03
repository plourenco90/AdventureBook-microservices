package com.pictet.adventureservice.infrastructure.client.book.dto;


import java.util.List;

public record PlayableSection(
        Integer id,
        String text,
        SectionType type,
        List<PlayableOption> options
) {
}
