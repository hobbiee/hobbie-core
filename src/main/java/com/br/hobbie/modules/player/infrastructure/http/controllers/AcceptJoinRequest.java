package com.br.hobbie.modules.player.infrastructure.http.controllers;

import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.infrastructure.http.dtos.request.AcceptJoinRequestForm;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class AcceptJoinRequest {

    @PersistenceContext
    private final EntityManager manager;
    private final EventRepository eventRepository;

    @PutMapping("/accept-join-request")
    @Transactional
    public ResponseEntity<?> perform(@Valid @RequestBody AcceptJoinRequestForm form) {
        // At this moment, it is not so clear what this method should do.
        // To be more specific, it is not clear how the flow of this feature should be, so I will try some pseudocode here to try to find a better way to implement this feature.c

        // the only one who can accept a new participant is the player admin of event, so basically we have a player accepting another player to join in his event.

        // for achieve this, in our request body we'll need to receive the id of the player who is accepting the request and the id of the player who is being accepted.
        var playerWhoIsAccepting = manager.find(Player.class, form.adminId());
        var playerWhoIsBeingAccepted = manager.find(Player.class, form.playerToAcceptId());

        Assert.state(playerWhoIsAccepting != null, "Something went wrong, maybe the entered player who is accepting does not exist");
        Assert.state(playerWhoIsBeingAccepted != null, "Something went wrong, maybe the entered player who is being accepted does not exist");

        return eventRepository.findById(form.eventId()).map(event -> {
            Either<RuntimeException, Void> acceptedOrError = playerWhoIsAccepting.acceptJoinRequest(playerWhoIsBeingAccepted, event);

            if (acceptedOrError.isLeft())
                return ResponseEntity.unprocessableEntity().body(acceptedOrError.getLeft().getMessage());

            manager.merge(playerWhoIsBeingAccepted);
            manager.merge(playerWhoIsAccepting);
            manager.flush();
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.unprocessableEntity().body("Something went wrong, maybe the entered event does not exist"));
    }
}
