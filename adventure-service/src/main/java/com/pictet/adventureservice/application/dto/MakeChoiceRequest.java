package com.pictet.adventureservice.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record MakeChoiceRequest(
        @NotNull(message = "Index cannot be null")
        @PositiveOrZero(message = "Index needs to be positive or zero")
        Integer optionIndex) {
}
