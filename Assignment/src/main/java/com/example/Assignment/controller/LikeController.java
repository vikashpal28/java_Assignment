package com.example.Assignment.controller;

import com.example.Assignment.dto.LikeCount;
import com.example.Assignment.dto.LikeResponse;
import com.example.Assignment.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeCount> updateLike(@PathVariable("postId") Long postId, @RequestBody LikeResponse response) {
        return ResponseEntity.ok().body(likeService.updateLike(postId , response));
    }


}
