package com.pictet.adventureservice.infrastructure.client.book.dto;

public record BookPlayableResponse(boolean valid, BookDetailsResponse book) {
}