package com.br.hobbie.modules.event.infrastructure.http.dtos.request;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.entities.ParticipationRequest;
import com.br.hobbie.modules.player.domain.entities.Player;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.util.Assert;

public record InterestRequest(
        @NotNull
        @Positive
        Long eventId,

        @NotNull
        @Positive
        Long playerId
) {

    public ParticipationRequest toEntity(EntityManager manager) {
        var event = manager.find(Event.class, eventId);
        var player = manager.find(Player.class, playerId);

        Assert.state(event != null, "Something went wrong, maybe the entered event does not exist");
        Assert.state(player != null, "Something went wrong, maybe the entered player does not exist");

        return new ParticipationRequest(player, event);
    }
}

