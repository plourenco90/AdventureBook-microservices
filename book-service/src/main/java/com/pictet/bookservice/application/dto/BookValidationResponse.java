package com.pictet.bookservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Book Validation Response")
public record BookValidationResponse(
        @Schema(description = "Book ID", example = "1")
        Long bookId,
        @Schema(description = "If the book is valid or not", example = "false")
        boolean valid,
        @Schema(description = "List of validation Errors")
        List<BookValidationErrorResponse> errors) {

    @Schema(description = "Validation Error of Book")
    public record BookValidationErrorResponse(
            @Schema(description = "The code of the validation error", example = "MULTIPLE_BEGINNINGS")
            String code,
            @Schema(description = "The message of the error", example = "Book cannot contain more than one beginning section.")
            String message,
            @Schema(description = "The section that contains the error", example = "1234")
            Integer sectionId) {

    }
}
