package com.pictet.bookservice.infrastructure.json.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsequenceJson(String type, String value, String text) {
}
