package com.br.hobbie.modules.player.infrastructure.http.dtos.request;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.entities.JoinRequest;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InterestRequest(
        @NotNull
        @Positive
        Long eventId,

        @NotNull
        @Positive
        Long playerId
) {

    public Either<IllegalStateException, JoinRequest> toEntity(EntityManager manager) {
        var event = manager.find(Event.class, eventId);
        var player = manager.find(Player.class, playerId);

        if (event == null || player == null) {
            return Either.left(new IllegalStateException("Something went wrong, maybe the entered event or player does not exist"));
        }

        return player.sendInterest(event);
    }
}

