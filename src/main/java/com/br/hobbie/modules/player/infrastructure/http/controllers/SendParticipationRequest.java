package com.br.hobbie.modules.player.infrastructure.http.controllers;

import com.br.hobbie.modules.player.infrastructure.http.dtos.request.InterestRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/players")
@RequiredArgsConstructor
public class SendParticipationRequest {

    @PersistenceContext
    private final EntityManager manager;

    @PostMapping("/interest")
    @Transactional
    public ResponseEntity<?> sendInterest(@Valid @RequestBody InterestRequest request) {
        var eitherRequest = request.toEntity(manager);

        if (eitherRequest.isLeft()) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body(eitherRequest.getLeft().getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
