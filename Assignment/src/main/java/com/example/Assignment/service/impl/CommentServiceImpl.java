package com.example.Assignment.service.impl;

import com.example.Assignment.dto.CommentDTO;
import com.example.Assignment.dto.CommentResponse;
import com.example.Assignment.entity.Author;
import com.example.Assignment.entity.Bot;
import com.example.Assignment.entity.Comment;
import com.example.Assignment.entity.Post;
import com.example.Assignment.entity.repository.AuthorRepository;
import com.example.Assignment.entity.repository.BotRepository;
import com.example.Assignment.entity.repository.CommentRepository;
import com.example.Assignment.entity.repository.PostRepository;
import com.example.Assignment.service.CommentService;
import com.example.Assignment.service.NotificationService;
import com.example.Assignment.service.ViralityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AuthorRepository authorRepository;
    private final ViralityService viralityService;
    private final StringRedisTemplate redisTemplate;
    private final NotificationService notificationService;

    // Atomic Lua script: increments only if value is below the cap.
    // Returns 1 if increment succeeded, 0 if cap was already reached.
    private static final String BOT_CAP_SCRIPT =
            "local current = tonumber(redis.call('GET', KEYS[1]) or '0') " +
                    "if current >= tonumber(ARGV[1]) then return 0 " +
                    "else redis.call('INCR', KEYS[1]) return 1 end";

    @Override
    public CommentDTO addComment(Long postId, CommentResponse commentResponse) {

        int newDepth = 0;

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        );

        Author author = authorRepository.findById(commentResponse.authorId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found")
        );

        Comment parentComment = null;

        if (commentResponse.parentId() != null) {
            parentComment = commentRepository.findById(commentResponse.parentId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "The comment you are replying to (ID: "
                                    + commentResponse.parentId() + ") does not exist."));

            // ── Cooldown Cap (atomic EXISTS check) ───────────────────────────
            if ("bot".equalsIgnoreCase(commentResponse.authorType()) &&
                    "user".equalsIgnoreCase(parentComment.getAuthorType())) {

                String cooldownKey = "cooldown:bot_" + author.getId()
                        + ":human_" + parentComment.getAuthor().getId();

                Boolean isOnCooldown = redisTemplate.hasKey(cooldownKey);
                if (Boolean.TRUE.equals(isOnCooldown)) {
                    throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                            "Cooldown active: This bot cannot reply to this human for 10 minutes.");
                }
                // SET with NX (set-if-not-exists) + TTL is atomic — no race condition here
                redisTemplate.opsForValue().setIfAbsent(cooldownKey, "active", 30, TimeUnit.SECONDS);
            }

            // Vertical Cap
            if (parentComment.getDepthLevel() >= 20) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Maximum thread depth of 20 levels reached.");
            }
            newDepth = parentComment.getDepthLevel() + 1;
        }

        // ── Horizontal Cap (atomic Lua script) ───────────────────────────────
        if ("bot".equalsIgnoreCase(commentResponse.authorType())) {
            String botKey = "post:" + postId + ":botCount";

            Long allowed = redisTemplate.execute(
                    new DefaultRedisScript<>(BOT_CAP_SCRIPT, Long.class),
                    Collections.singletonList(botKey),
                    "100"
            );

            if (allowed == null || allowed == 0L) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                        "This post has reached the maximum of 100 bot replies.");
            }

            viralityService.incrementScore(postId, "bot_reply");

        } else if ("user".equalsIgnoreCase(commentResponse.authorType())) {
            viralityService.incrementScore(postId, "human_comment");
        }

        // ── Persist comment ───────────────────────────────────────────────────
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(commentResponse.content());
        comment.setAuthorType(commentResponse.authorType());
        comment.setDepthLevel(newDepth);
        comment.setParent(parentComment);

        Comment savedComment = commentRepository.save(comment);
        if (parentComment != null && "user".equalsIgnoreCase(parentComment.getAuthorType())) {
            // Ensure you have a clean way to get the bot name
            String senderName = (author instanceof Bot) ? ((Bot) author).getName() : "System Bot";
            notificationService.processNotification(parentComment.getAuthor().getId(), senderName);
        }
        return new CommentDTO(
                savedComment.getId(),
                postId,
                commentResponse.authorId(),
                savedComment.getAuthorType(),
                savedComment.getContent(),
                savedComment.getDepthLevel(),
                savedComment.getParent() != null ? savedComment.getParent().getId() : null,
                savedComment.getCreated_at()
        );
    }
}