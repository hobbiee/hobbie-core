package com.br.hobbie.modules.player.infrastructure.http.controllers;

import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.infrastructure.http.dtos.request.RejectJoinRequestForm;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
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

        Assert.notNull(admin, "Check if you are passing the correct admin id");
        Assert.notNull(joiningPlayer, "Check if you are passing the correct player to reject id");

        var event = eventRepository.findById(form.eventId());

        if (event.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body("Check if you are passing the correct event id");
        }

        admin.rejectJoinRequest(joiningPlayer, event.get());
        return ResponseEntity.ok().build();
    }
}
