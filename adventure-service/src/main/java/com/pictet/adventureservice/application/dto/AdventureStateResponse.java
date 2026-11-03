package com.pictet.adventureservice.application.dto;

import com.pictet.adventureservice.infrastructure.client.book.dto.SectionType;
import com.pictet.adventureservice.domain.model.AdventureStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Adventure State")
public record AdventureStateResponse(
        @Schema(description = "Adventure ID", example = "1")
        Long id,
        @Schema(description = "Book ID", example = "123")
        Long bookId,
        @Schema(description = "Player Name", example = "John")
        String playerName,
        @Schema(description = "Player Health", example = "10")
        int health,
        @Schema(description = "Adventure Status (ACTIVE, COMPLETED, DEAD)", example = "ACTIVE")
        AdventureStatus status,
        @Schema(description = "Current Section")
        CurrentSectionResponse currentSection,
        @Schema(description = "Consequence Message", example = "You fell and lost 3HP")
        String consequenceMessage) {

    @Schema(description = "Current Section")
    public record CurrentSectionResponse(
            @Schema(description = "Section ID", example = "5000")
            Integer id,
            @Schema(description = "Section Text", example = "You see a door")
            String text,
            @Schema(description = "Section Type (BEGIN, NODE, END)", example = "BEGIN")
            SectionType type,
            @Schema(description = "List of choices")
            List<ChoiceResponse> choices) {
    }

    @Schema(description = "Choice")
    public record ChoiceResponse(
            @Schema(description = "Index of the choice", example = "0")
            int index,
            @Schema(description = "Text of the choice", example = "Use the key.")
            String description) {
    }
}
