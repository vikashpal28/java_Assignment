package com.example.Assignment.dto;

public record CommentResponse(
    Long authorId,
    String authorType,
    String content,
    Long parentId
) {
}
