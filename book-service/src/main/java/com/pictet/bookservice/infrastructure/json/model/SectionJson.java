package com.pictet.bookservice.infrastructure.json.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import tools.jackson.databind.JsonNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SectionJson(JsonNode id, String text, String type,
                          @JsonSetter(nulls = Nulls.AS_EMPTY) List<OptionJson> options) {
}