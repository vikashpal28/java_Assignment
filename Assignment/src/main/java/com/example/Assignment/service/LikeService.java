package com.example.Assignment.service;

import com.example.Assignment.dto.LikeCount;
import com.example.Assignment.dto.LikeResponse;

public interface LikeService {
    LikeCount updateLike(Long postId, LikeResponse response);
}
