package com.example.Assignment.controller;

import com.example.Assignment.dto.PostResponse;
import com.example.Assignment.entity.Post;
import com.example.Assignment.service.FunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FunctionController {

    private final FunctionService functionService;

    @PostMapping("/posts")
    public ResponseEntity<Post> postMethod(@RequestBody PostResponse response){
        return ResponseEntity.ok(functionService.postMethod(response));
    }

}
