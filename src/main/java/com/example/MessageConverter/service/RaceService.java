package com.example.MessageConverter.service;

import com.example.MessageConverter.entity.Race;
import com.example.MessageConverter.repository.RaceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RaceService {
    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public Race getRaceByValueTag(String raceValue) {
        Optional<Race> isRace = raceRepository.findByValueTag(raceValue);
        if (isRace.isEmpty()) {
            throw new RuntimeException("RACE NOT FOUND BY VALUE");
        }
        return isRace.get();
    }
}
