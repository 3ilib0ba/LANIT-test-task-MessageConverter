package com.example.MessageConverter;

import com.example.MessageConverter.entity.AuthorPost;
import com.example.MessageConverter.entity.LetterAuthor;
import com.example.MessageConverter.entity.Race;
import com.example.MessageConverter.repository.AuthorPostRepository;
import com.example.MessageConverter.repository.LetterAuthorRepository;
import com.example.MessageConverter.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;

@SpringBootApplication
public class MessageConverterApplication implements CommandLineRunner {
    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private LetterAuthorRepository letterAuthorRepository;

    @Autowired
    private AuthorPostRepository authorPostRepository;


    public static void main(String[] args) {
        SpringApplication.run(MessageConverterApplication.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        // Init static objects about future converter
        List<Race> races = List.of(
                new Race(null, "A1", "Официальное письмо марсианам от жителей Земли", "Добыча полезных ископаемых"),
                new Race(null, "A2", "Официальное письмо народу Татуина от землян", "Озеленение пустыни"),
                new Race(null, "A3", "Официальное письмо воганам от жителей Земли", "Расширение межзвездной навигации")
        );
        raceRepository.saveAll(races);

        List<LetterAuthor> letterAuthors = List.of(
			    new LetterAuthor(null, "ISO_639-1", "Иванов", "Иван", "Иванович", "7432234555"),
			    new LetterAuthor(null, "ISO_965-44", "Сидорова", "Наталья", "Николаевна", "3676545567"),
			    new LetterAuthor(null, "ISO_139-5", "Петров", "Перт", "Петрович", "6575677888"),
			    new LetterAuthor(null, "ISO_8568-51", "Плюшкина", "Ирина", "", "7432234555")
        );
        letterAuthorRepository.saveAll(letterAuthors);

        List<AuthorPost> authorPosts = List.of(
                new AuthorPost(null, "7432234555", "Старший научный сотрудник по добыче полезных ископаемых"),
                new AuthorPost(null, "3676545567", "Директор департамента озеленения пустынь"),
                new AuthorPost(null, "6575677888", "Главный инженер по строительству межзвездных автострад"),
                new AuthorPost(null, "9655677677", "Руководитель отдела взаимодействия с внеземными цивилизациями")
        );
        authorPostRepository.saveAll(authorPosts);

    }

}
