package com.example.Assignment.service;

import com.example.Assignment.dto.PostResponse;
import com.example.Assignment.entity.Post;

public interface FunctionService {
    Post postMethod(PostResponse response);
}
