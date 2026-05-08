package com.example.Assignment.service;

import com.example.Assignment.dto.CommentDTO;
import com.example.Assignment.dto.CommentResponse;

public interface CommentService {
    CommentDTO addComment(Long postId, CommentResponse commentResponse);
}
