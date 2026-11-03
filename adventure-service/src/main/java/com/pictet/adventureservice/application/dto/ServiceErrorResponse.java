package com.pictet.adventureservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error Response")
public record ServiceErrorResponse(
        @Schema(description = "Error Code")
        String code,
        @Schema(description = "Error Message")
        String message) {


}
