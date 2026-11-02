package com.pictet.bookservice.domain.validation;

import java.util.List;

public record BookValidationResult(List<BookValidationError> errors) {

    public BookValidationResult {
        errors = List.copyOf(errors);
    }
}
