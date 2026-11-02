package com.pictet.bookservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Playable Book Response")
public record BookPlayableResponse(
        @Schema(description = "If the book is valid", example = "true")
        boolean valid,
        @Schema(description = "The Book details")
        BookResponse book) {
}