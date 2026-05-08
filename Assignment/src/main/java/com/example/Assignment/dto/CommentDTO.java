package com.example.Assignment.dto;

import com.example.Assignment.entity.Comment;

import java.time.Instant;

public record CommentDTO(
        Long id,
        Long postId,
        Long authorId,
        String authorType,
        String content,
        int depthLevel,
        Long parentId,
        Instant createdAt
) {}