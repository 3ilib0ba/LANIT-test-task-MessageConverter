package com.example.MessageConverter.repository;

import com.example.MessageConverter.entity.AuthorPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorPostRepository extends JpaRepository<AuthorPost, Long> {
    Optional<AuthorPost> findByPost(String post);

}
