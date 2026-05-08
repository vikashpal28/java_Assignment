package com.example.Assignment.service.impl;

import com.example.Assignment.dto.BotResponse;
import com.example.Assignment.dto.UserResponse;
import com.example.Assignment.entity.Bot;
import com.example.Assignment.entity.User;
import com.example.Assignment.entity.repository.BotRepository;
import com.example.Assignment.entity.repository.UserRepository;
import com.example.Assignment.service.UserBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBotServiceImpl implements UserBotService {
    private final UserRepository userRepository;
    private final BotRepository botRepository;

    @Override
    public User addUser(UserResponse userResponse) {
        User user = new User();
        user.setUsername(userResponse.username());
        user.setIsPremium(userResponse.isPremium());
        return userRepository.save(user);
    }

    @Override
    public Bot addBot(BotResponse botResponse) {
        Bot bot = new Bot();
        bot.setName(botResponse.name());
        bot.setPersonalDescription(botResponse.personalDescription());

        // This saves to both 'author' and 'bot' tables automatically
        return botRepository.save(bot);
    }
}
