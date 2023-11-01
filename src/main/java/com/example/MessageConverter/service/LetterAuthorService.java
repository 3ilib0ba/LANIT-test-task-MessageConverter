package com.example.MessageConverter.service;

import com.example.MessageConverter.entity.LetterAuthor;
import com.example.MessageConverter.repository.LetterAuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LetterAuthorService {
    private final LetterAuthorRepository letterAuthorRepository;

    public LetterAuthorService(LetterAuthorRepository letterAuthorRepository) {
        this.letterAuthorRepository = letterAuthorRepository;
    }

    public LetterAuthor getAuthorByAuthorId(String authorId) {
        Optional<LetterAuthor> isAuthor = letterAuthorRepository.findByAuthorId(authorId);
        if (isAuthor.isEmpty()) {
            throw new RuntimeException("AUTHOR NOT FOUND BY VALUE");
        }
        return isAuthor.get();
    }
}
