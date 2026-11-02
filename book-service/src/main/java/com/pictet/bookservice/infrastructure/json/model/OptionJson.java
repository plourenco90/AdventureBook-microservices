package com.pictet.bookservice.infrastructure.json.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OptionJson(String description, JsonNode gotoId, ConsequenceJson consequence) {
}