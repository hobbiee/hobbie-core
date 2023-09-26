package com.br.hobbie.modules.event.infrastructure.http.controllers;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.event.infrastructure.http.dtos.request.CreateEventRequest;
import com.br.hobbie.modules.player.domain.repositories.PlayerRepository;
import com.br.hobbie.shared.core.errors.Either;
import com.br.hobbie.shared.core.validators.BeforeThan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("v1/api/events")
@RequiredArgsConstructor
public class CreateEvent {

    private final EventRepository repository;
    private final PlayerRepository playerRepository;

    @PostMapping
    public ResponseEntity<Void> handle(@Valid @RequestBody CreateEventRequest request) {
        Either<RuntimeException, Event> eventOrError = request.toEntity(playerRepository::findById);

        if (eventOrError.isLeft()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        repository.save(eventOrError.getRight());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/local-time")
    public ResponseEntity<LocalTime> getLocalTime() {
        return ResponseEntity.ok(LocalTime.now());
    }

    @PostMapping("/local-time")
    public ResponseEntity<LocalTime> postLocalTime(@Valid @RequestBody LocalTimeRequest request) {
        System.out.println(request.time());
        return ResponseEntity.ok(request.time());
    }
}

@BeforeThan(field = "otherTime", value = "time")
record LocalTimeRequest(
        @Future LocalTime time,

        @Future
        LocalTime otherTime,

        String name

) {

}
