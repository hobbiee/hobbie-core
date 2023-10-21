package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.shared.core.errors.Either;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventTest {
    private Event event;
    private Player player;

    @BeforeEach
    void setUp() {
        event = PlayerEventTestFactory.createEvent();
        player = PlayerEventTestFactory.createPlayer();
    }

    @Test
    @DisplayName("Should add participant if capacity is not reached")
    void addParticipant_WhenCapacityNotReached() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();

        // WHEN
        event.addParticipant(player);

        // THEN
        // two participants because the admin is also a participant
        Assertions.assertEquals(2, event.getParticipants().size());
        Assertions.assertTrue(event.getParticipants().contains(player));
    }

    @Test
    @DisplayName("Should not add participant if capacity is reached")
    void addParticipant_WhenCapacityReached() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();
        for (int i = 0; i < 10; i++) {
            event.addParticipant(PlayerEventTestFactory.createParticipant());
        }

        // WHEN
        Either<RuntimeException, Boolean> result = event.addParticipant(player);

        // THEN
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Event is full", result.getLeft().getMessage());
    }

    @Test
    @DisplayName("Should not add participant if the player is the admin")
    void addParticipant_WhenPlayerIsAdmin() {
        // GIVEN
        Player player = event.getAdmin();

        // WHEN / THEN
        Assertions.assertThrows(IllegalStateException.class, () -> event.addParticipant(player));
    }


    @Test
    @DisplayName("Should return true if capacity is reached")
    void isFull() {
        // GIVEN
        for (int i = 0; i < 10; i++) {
            event.addParticipant(PlayerEventTestFactory.createParticipant());
        }

        // WHEN
        boolean result = event.capacityReached();

        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false if has capacity")
    void isNotFull() {
        // GIVEN
        // 8 participants because the admin is also a participant
        for (int i = 0; i < 8; i++) {
            event.addParticipant(PlayerEventTestFactory.createParticipant());
        }

        // WHEN
        boolean result = event.capacityReached();

        // THEN
        Assertions.assertFalse(result);
    }
}