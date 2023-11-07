package com.br.hobbie.modules.event.infrastructure.http.controllers;

import com.br.hobbie.modules.event.application.DistanceCalculator;
import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.event.infrastructure.http.dtos.response.EventResponse;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.ports.ExistentTagsResolver;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/events")
@RequiredArgsConstructor
public class FindEventsMatchingWithInterests {

    private final EventRepository eventRepository;
    private final DistanceCalculator distanceCalculator;
    private final EntityManager manager;
    private final ExistentTagsResolver existentTagsResolver;

    @GetMapping("/{playerId}")
    public ResponseEntity<Set<EventResponse>> perform(@Valid @PathVariable @NotNull @Positive Long playerId) {
        Player player = manager.find(Player.class, playerId);

        Assert.notNull(player, "Player not found");

        Collection<Event> events = eventRepository.findAllMatchingWithInterests(playerId);

        events.removeIf(event -> !distanceCalculator.isWithinRadius(player, event));

        var tags = existentTagsResolver.extractDistinctTags(events, player);

        var possibleRecommendations = eventRepository.findAllByCategoriesNamesContainingIgnoreCase(tags.stream().map(Tag::getName).toList(), playerId);

        possibleRecommendations.removeIf(event -> !distanceCalculator.isWithinRadius(player, event));

        events.addAll(possibleRecommendations);

        events.removeIf(event -> event.hasPendingOrAcceptedJoinRequestFrom(player) || event.isParticipant(player));

        return ResponseEntity.ok(events.stream().map(event -> EventResponse.from(event, player, distanceCalculator)).collect(Collectors.toSet()));
    }

}
