package com.example.Assignment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationSweeper {

    private final StringRedisTemplate redisTemplate;

    // Runs every 5 minutes (300,000 milliseconds)
    @Scheduled(fixedRate = 300000)
    public void sweepPendingNotifications() {
        // 1. Find all keys matching the pending notifications pattern
        Set<String> keys = redisTemplate.keys("user:*:pending_notifs");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String listKey : keys) {
            // Extract userId from the key "user:{userId}:pending_notifs"
            String userId = listKey.split(":")[1];

            // 2. Get the size of the pending list
            Long size = redisTemplate.opsForList().size(listKey);

            if (size != null && size > 0) {
                // 3. Pop the first bot name to show in the summary
                String firstBot = redisTemplate.opsForList().leftPop(listKey);
                int othersCount = (int) (size - 1);

                // 4. Log the summarized message
                if (othersCount > 0) {
                    System.out.println("Summarized Push Notification: " + firstBot +
                            " and [" + othersCount + "] others interacted with your posts (User " + userId + ").");
                } else {
                    System.out.println("Summarized Push Notification: " + firstBot +
                            " interacted with your posts (User " + userId + ").");
                }

                // 5. Clear the remaining list for this user
                redisTemplate.delete(listKey);
            }
        }
    }
}