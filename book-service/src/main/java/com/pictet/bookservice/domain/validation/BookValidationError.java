package com.pictet.bookservice.domain.validation;

public record BookValidationError(String code, String message, Integer sectionId) {
}
