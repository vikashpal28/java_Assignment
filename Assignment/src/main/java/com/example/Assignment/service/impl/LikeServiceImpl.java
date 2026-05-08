package com.example.Assignment.service.impl;

import com.example.Assignment.dto.LikeCount;
import com.example.Assignment.dto.LikeResponse;
import com.example.Assignment.entity.Post;
import com.example.Assignment.entity.repository.PostRepository;
import com.example.Assignment.service.LikeService;
import com.example.Assignment.service.ViralityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final PostRepository postRepository;
    private final ViralityService viralityService;
    private final StringRedisTemplate redisTemplate;
    @Override
    public LikeCount updateLike(Long postId, LikeResponse response) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RuntimeException("post not found")
        );
      if("human_like".equalsIgnoreCase(response.humanLike())){
         viralityService.incrementScore(postId , "human_like");
      }
      else {
          System.out.println("Condition FAILED. Score will not increase.");
      }

      String key = "post:"+postId+":virality_score";
      String currentScore = redisTemplate.opsForValue().get(key);

      Long score = (currentScore != null) ? Long.parseLong(currentScore) : 0L;

      return new LikeCount(score);
    }
}
