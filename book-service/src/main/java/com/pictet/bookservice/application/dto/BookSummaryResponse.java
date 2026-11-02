package com.pictet.bookservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Book Summary information")
public record BookSummaryResponse(
        @Schema(description = "Book ID", example = "1")
        Long id,
        @Schema(description = "Title of the Book", example = "Prisoner")
        String title,
        @Schema(description = "Author of the Book", example = "John Doe")
        String author,
        @Schema(description = "If the book is valid or not", example = "true")
        boolean valid) {
}