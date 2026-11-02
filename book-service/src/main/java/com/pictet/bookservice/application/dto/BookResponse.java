package com.pictet.bookservice.application.dto;

import com.pictet.bookservice.domain.model.ConsequenceType;
import com.pictet.bookservice.domain.model.SectionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Book Details")
public record BookResponse(
        @Schema(description = "Book ID", example = "1")
        Long id,
        @Schema(description = "Title of the Book", example = "Prisoner")
        String title,
        @Schema(description = "Author of the Book", example = "John Doe")
        String author,
        @Schema(description = "List of sections of the book")
        List<SectionResponse> sections) {

    @Schema(description = "Section Details")
    public record SectionResponse(
            @Schema(description = "Section ID", example = "1")
            Long id,
            @Schema(description = "Text", example = "You open the door")
            String text,
            @Schema(description = "The type of section (BEGIN, NODE, END)", example = "NODE")
            SectionType type,
            @Schema(description = "List of Options")
            List<OptionResponse> options) {
    }

    @Schema(description = "Option Details")
    public record OptionResponse(
            @Schema(description = "The option description", example = "Try to scan the area with your hands.")
            String description,
            @Schema(description = "The option index", example = "0")
            int optionIndex,
            @Schema(description = "The ID of the section to go", example = "500")
            Integer goToId,
            @Schema(description = "The consequence of the option")
            ConsequenceResponse consequence) {
    }

    @Schema(description = "Consequence Details")
    public record ConsequenceResponse(
            @Schema(description = "The type of consequence (LOSE_HEALTH)", example = "LOSE_HEALTH")
            ConsequenceType type,
            @Schema(description = "The effect on the value", example = "3")
            Integer value,
            @Schema(description = "The consequence text", example = "As you move your hands left and right under the bed, you cut yourself on a rusty nail.")
            String text) {
    }
}
