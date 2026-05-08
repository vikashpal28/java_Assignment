package com.example.Assignment.dto;

public record PostResponse(
        Long authorId,
        String authorType,
        String content
) {
}
