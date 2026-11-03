package com.pictet.adventureservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StartAdventureRequest(
        @NotNull(message = "The book ID cannot be null")
        Long bookId,
        @NotBlank(message = "The player name cannot be empty")
        String playerName) {
}
