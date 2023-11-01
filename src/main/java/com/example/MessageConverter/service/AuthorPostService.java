package com.example.MessageConverter.service;

import com.example.MessageConverter.entity.AuthorPost;
import com.example.MessageConverter.repository.AuthorPostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorPostService {
    private final AuthorPostRepository authorPostRepository;

    public AuthorPostService(AuthorPostRepository authorPostRepository) {
        this.authorPostRepository = authorPostRepository;
    }

    public AuthorPost getPositionByAuthorId(String posId) {
        Optional<AuthorPost> isPos = authorPostRepository.findByPost(posId);
        if (isPos.isEmpty()) {
            throw new RuntimeException("POSITION NOT FOUND BY VALUE");
        }
        return isPos.get();
    }

}
