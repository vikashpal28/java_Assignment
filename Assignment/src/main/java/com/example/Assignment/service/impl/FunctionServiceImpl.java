package com.example.Assignment.service.impl;

import com.example.Assignment.dto.PostResponse;
import com.example.Assignment.entity.Author;
import com.example.Assignment.entity.Bot;
import com.example.Assignment.entity.Post;
import com.example.Assignment.entity.repository.BotRepository;
import com.example.Assignment.entity.repository.PostRepository;
import com.example.Assignment.entity.repository.UserRepository;
import com.example.Assignment.service.FunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FunctionServiceImpl implements FunctionService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BotRepository botRepository;


    @Override
    public Post postMethod(PostResponse response) {
        Post post = new Post();
        post.setContent(response.content());
        post.setAuthorType(response.authorType());

        if("user".equalsIgnoreCase(response.authorType())){
            Author author = userRepository.findById(response.authorId()).orElseThrow(
                    () -> new RuntimeException("user not found")
            );
            post.setAuthor(author);
        }
        else if("bot".equalsIgnoreCase(response.authorType())){
            Bot bot = botRepository.findById(response.authorId()).orElseThrow(
                    () -> new RuntimeException("bot not found")
            );
            post.setAuthor(bot);
        }
      Post updatedPost =  postRepository.save(post);
        return updatedPost;
    }
}
