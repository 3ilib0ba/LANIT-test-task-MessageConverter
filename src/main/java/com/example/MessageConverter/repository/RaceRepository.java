package com.example.MessageConverter.repository;

import com.example.MessageConverter.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {
    Optional<Race> findByValueTag(String valueTag);

}
