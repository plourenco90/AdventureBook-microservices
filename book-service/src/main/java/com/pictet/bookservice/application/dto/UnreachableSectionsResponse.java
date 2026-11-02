package com.pictet.bookservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Unreachable Sections Response")
public record UnreachableSectionsResponse(
        @Schema(description = "Book ID", example = "1")
        Long bookId,
        @Schema(description = "Unreachable Sections List of ID's")
        List<Integer> unreachableSectionIds) {

    public UnreachableSectionsResponse {
        unreachableSectionIds = List.copyOf(unreachableSectionIds);
    }
}
