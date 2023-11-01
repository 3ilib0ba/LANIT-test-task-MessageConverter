package com.example.MessageConverter.repository;

import com.example.MessageConverter.entity.LetterAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LetterAuthorRepository extends JpaRepository<LetterAuthor, Long>{
    Optional<LetterAuthor> findByAuthorId(String authorId);

}
