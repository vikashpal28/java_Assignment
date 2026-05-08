package com.example.Assignment.service.impl;

import com.example.Assignment.service.ViralityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViralityServiceImpl implements ViralityService {

    private final StringRedisTemplate redisTemplate;

    public void incrementScore(Long postId , String type){
        String key = "post:" + postId + ":virality_score";

        long point = switch (type.toLowerCase()){
            case "bot_reply" -> 1L;
            case "human_like" -> 20L;
            case "human_comment" -> 50L;
            default -> 0L;
        };
        if(point > 0L) redisTemplate.opsForValue().increment(key, point);
    }



}
