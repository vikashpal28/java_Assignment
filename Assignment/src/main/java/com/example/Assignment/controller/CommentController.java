package com.example.Assignment.controller;

import com.example.Assignment.dto.CommentDTO;
import com.example.Assignment.dto.CommentResponse;
import com.example.Assignment.entity.Comment;
import com.example.Assignment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable("postId") Long postId, @RequestBody CommentResponse  commentResponse) {
        return ResponseEntity.ok().body(commentService.addComment(postId , commentResponse));
    }
}
