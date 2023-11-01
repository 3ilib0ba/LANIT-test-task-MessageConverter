package com.example.MessageConverter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String post;

    private String description;
}
