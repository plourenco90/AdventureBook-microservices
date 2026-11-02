package com.pictet.bookservice.infrastructure.json.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookJson(Long id, String title, String author, List<SectionJson> sections) {
}