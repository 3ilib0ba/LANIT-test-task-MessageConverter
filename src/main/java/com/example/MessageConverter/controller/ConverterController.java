package com.example.MessageConverter.controller;

import com.example.MessageConverter.repository.RaceRepository;
import com.example.MessageConverter.service.ConverterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConverterController {
    private final ConverterService converterService;
    private final RaceRepository raceRepository;


    public ConverterController(
            RaceRepository raceRepository,
            ConverterService converterService
    ) {
        this.raceRepository = raceRepository;
        this.converterService = converterService;
    }


    @GetMapping(value = "/ping")
    public String ping() {
        return "Hello, world!";
    }

//    @GetMapping("/races")
//    public String getAllRaces() {
//        List<Race> allRaces = raceRepository.findAll();
//        return Arrays.toString(allRaces.toArray());
//    }
//
//    @PostMapping("/races")
//    public String saveRace(@RequestBody Race race) {
//        raceRepository.save(race);
//        return "New race saved: " + race.toString();
//    }

    @PostMapping(
            value = "/convert",
            consumes = "application/xml",
            produces = "application/xml"
    )
    public ResponseEntity<String> convertMessage(
            @RequestBody String message
    ) {
        return ResponseEntity.ok(converterService.convertMessage(message));
    }

}
