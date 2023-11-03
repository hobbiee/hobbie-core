package com.br.hobbie.modules.player.infrastructure.http.controllers;

import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.infrastructure.http.dtos.request.RejectJoinRequestForm;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/players")
@RequiredArgsConstructor
public class RejectJoinRequest {

    private final EntityManager manager;
    private final EventRepository eventRepository;

    @PutMapping("/reject-join-request")
    @Transactional
    public ResponseEntity<?> perform(@Valid @RequestBody RejectJoinRequestForm form) {
        var admin = manager.find(Player.class, form.adminId());
        var joiningPlayer = manager.find(Player.class, form.playerToRejectId());

        if (admin == null)
            return ResponseEntity.unprocessableEntity().body("Check if you are passing the correct admin id");

        if (joiningPlayer == null)
            return ResponseEntity.unprocessableEntity().body("Check if you are passing the correct player to reject id");

        var event = eventRepository.findById(form.eventId());

        if (event.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body("Check if you are passing the correct event id");
        }

        Either<RuntimeException, Boolean> result = admin.rejectJoinRequest(joiningPlayer, event.get());

        if (result.isLeft()) {
            return ResponseEntity.unprocessableEntity().body(result.getLeft().getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
