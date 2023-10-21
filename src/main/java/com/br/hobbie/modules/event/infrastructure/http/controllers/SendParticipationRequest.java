package com.br.hobbie.modules.event.infrastructure.http.controllers;

import com.br.hobbie.modules.event.infrastructure.http.dtos.request.InterestRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/events")
@RequiredArgsConstructor
public class SendParticipationRequest {

    @PersistenceContext
    private final EntityManager manager;

    @PostMapping("/interest")
    @Transactional
    public void sendInterest(@Valid @RequestBody InterestRequest request) {
        var participationRequest = request.toEntity(manager);
        manager.persist(participationRequest);
    }
}
