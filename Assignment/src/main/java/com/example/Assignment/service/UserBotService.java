package com.example.Assignment.service;

import com.example.Assignment.dto.BotResponse;
import com.example.Assignment.dto.UserResponse;
import com.example.Assignment.entity.Bot;
import com.example.Assignment.entity.User;

public interface UserBotService {
    User addUser(UserResponse userResponse);

    Bot addBot(BotResponse botResponse);
}
