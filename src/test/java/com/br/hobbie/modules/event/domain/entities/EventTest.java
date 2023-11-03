package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.errors.Either;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link Event}
 */
@ExtendWith(MockitoExtension.class)
class EventTest {
    private Event event;

    @BeforeEach
    void setUp() {
        event = PlayerEventTestFactory.createEvent();
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
        Assertions.assertEquals(2, event.getAmountOfParticipants());
        Assertions.assertTrue(event.isParticipant(player));
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

    @Test
    @DisplayName("Should return true when player already sent a request")
    void requestAlreadySent() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();
        player.sendInterest(event);

        // WHEN
        boolean result = event.requestAlreadySent(player);

        // THEN
        Assertions.assertTrue(result);
    }

    /**
     * <h2>
     * What is the meaning of this test?
     * </h2>
     *
     * <p>
     * Overlapping events on hobbie context means that the player cannot send interest to an event that starts at least 1 hour before or after the event
     * </p>
     */
    @Test
    @DisplayName("Should return true when an event overlaps with another event")
    void shouldReturnTrue_WhenEventOverlapsWithAnotherEvent() {
        // GIVEN
        var overlappingEvent = PlayerEventTestFactory.createOverlappingEvent();

        // WHEN
        boolean result = event.overlapsWith(overlappingEvent);

        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when an event does not overlap with another event")
    void shouldReturnFalse_WhenEventDoesNotOverlapWithAnotherEvent() {
        // GIVEN
        var nonOverlappingEvent = PlayerEventTestFactory.createNonOverlappingEvent();

        // WHEN
        boolean result = event.overlapsWith(nonOverlappingEvent);

        // THEN
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("It should returns true when the player has a join request")
    void shouldReturnTrue_WhenPlayerHasJoinRequest() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();
        player.sendInterest(event);

        // WHEN
        boolean result = event.hasJoinRequestFrom(player);

        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should accept join request")
    void shouldAcceptJoinRequest() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();
        player.sendInterest(event);

        // WHEN
        event.acceptJoinRequest(player);

        // THEN
        Assertions.assertTrue(event.isParticipant(player));
    }

    @Test
    @DisplayName("Should throw exception when accepting join request from a player that has not sent a request")
    void shouldThrowException_WhenAcceptingJoinRequestFromAPlayerThatHasNotSentARequest() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();

        // WHEN / THEN
        Assertions.assertThrows(IllegalStateException.class, () -> event.acceptJoinRequest(player));
    }

    @Test
    @DisplayName("Should reject join request")
    void shouldRejectJoinRequest() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();
        player.sendInterest(event);

        // WHEN
        event.rejectJoinRequest(player);

        // THEN
        Assertions.assertFalse(event.isParticipant(player));
    }

    @Test
    @DisplayName("Should throw exception when rejecting join request from a player that has not sent a request")
    void shouldThrowException_WhenRejectingJoinRequestFromAPlayerThatHasNotSentARequest() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();

        // WHEN / THEN
        Assertions.assertThrows(IllegalStateException.class, () -> event.rejectJoinRequest(player));
    }

    @Test
    @DisplayName("Should return true if a player is participating of event")
    void shouldReturnTrue_WhenPlayerIsParticipatingOfEvent() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();
        event.addParticipant(player);

        // WHEN
        boolean result = event.isParticipant(player);

        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return the event categories that player does not have interest")
    void shouldReturnTheEventCategoriesThatPlayerDoesNotHaveInterest() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();
        Tag tag3 = new Tag("tag3");
        player.addInterest(tag3);
        Tag tag4 = new Tag("tag4");
        player.addInterest(tag4);
        player.sendInterest(event);

        // WHEN
        var result = event.distinctTagsFrom(player);

        // THEN
        Assertions.assertEquals(2, result.size());
        Assertions.assertFalse(result.contains(tag3));
        Assertions.assertFalse(result.contains(tag4));
    }
}