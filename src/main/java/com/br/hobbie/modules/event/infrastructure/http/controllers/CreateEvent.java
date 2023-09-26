package com.br.hobbie.modules.event.infrastructure.http.controllers;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.event.infrastructure.http.dtos.request.CreateEventRequest;
import com.br.hobbie.modules.player.domain.repositories.PlayerRepository;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

