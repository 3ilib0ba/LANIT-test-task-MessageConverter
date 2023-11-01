package com.example.MessageConverter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetterAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String authorId;

    private String lastname;

    private String name;

    private String fathername;

    private String post;
}
