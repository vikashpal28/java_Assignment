package com.example.Assignment.service.impl;

import com.example.Assignment.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void processNotification(Long userId, String botName) {

        String cooldownKey = "notif_cooldown:user_" + userId;
        String listKey     = "user:" + userId + ":pending_notifs";
        String message     = "Bot " + botName + " replied to your post";

        // Atomic SET NX — returns true only if key did NOT exist (cooldown was inactive)
        Boolean justActivated = redisTemplate.opsForValue()
                .setIfAbsent(cooldownKey, "active", 15, TimeUnit.MINUTES);

        if (Boolean.TRUE.equals(justActivated)) {
            // Cooldown was inactive → send notification immediately

            // First, drain any previously queued messages
            Long pendingCount = redisTemplate.opsForList().size(listKey);
            if (pendingCount != null && pendingCount > 0) {
                System.out.println("Sending " + pendingCount
                        + " batched pending notifications to User " + userId);

                // Pop and print each pending notification
                for (int i = 0; i < pendingCount; i++) {
                    String pending = redisTemplate.opsForList().leftPop(listKey);
                    if (pending != null) {
                        System.out.println("Push Notification Sent to User "
                                + userId + ": " + pending);
                    }
                }
            }

            // Now send the current notification
            System.out.println("Push Notification Sent to User " + userId + ": " + message);

        } else {
            // Cooldown is active → queue for later
            redisTemplate.opsForList().rightPush(listKey, message);
            System.out.println("Notification queued for User " + userId
                    + " (cooldown active). Queue size: "
                    + redisTemplate.opsForList().size(listKey));
        }
    }
}