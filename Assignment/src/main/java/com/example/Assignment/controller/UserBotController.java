package com.example.Assignment.controller;

import com.example.Assignment.dto.BotResponse;
import com.example.Assignment.dto.UserResponse;
import com.example.Assignment.entity.Bot;
import com.example.Assignment.entity.User;
import com.example.Assignment.service.UserBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserBotController {

    private final UserBotService userBotService;

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody UserResponse userResponse){
        return ResponseEntity.ok().body(userBotService.addUser(userResponse));
    }

    @PostMapping("/bot")
    public ResponseEntity<Bot> addBot(@RequestBody BotResponse botResponse){
        return ResponseEntity.ok().body(userBotService.addBot(botResponse));
    }

}
