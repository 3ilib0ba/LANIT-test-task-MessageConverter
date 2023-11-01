package com.example.MessageConverter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String valueTag;

    private String toDescription;

    private String toTitle;
}
