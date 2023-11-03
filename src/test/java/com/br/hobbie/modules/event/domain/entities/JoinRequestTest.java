package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.event.application.RequestExpiration;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import com.br.hobbie.shared.utils.PlayerEventTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class JoinRequestTest {

    private static JoinRequest joinRequest;
    private static Player participant;
    private static Event event;

    @BeforeEach
    void setUp() {
        participant = PlayerEventTestFactory.createParticipant();
        event = PlayerEventTestFactory.createEvent();
        joinRequest = new JoinRequest(participant, event);
    }

    @Test
    @DisplayName("Should return true when the join request is from the player")
    void shouldReturnTrueWhenTheJoinRequestIsFromThePlayer() {
        // GIVEN
        Player player = PlayerEventTestFactory.createParticipant();

        // WHEN
        boolean result = joinRequest.isFrom(player);

        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when the join request is not from the player")
    void shouldReturnFalseWhenTheJoinRequestIsNotFromThePlayer() {
        // GIVEN
        Player player = PlayerEventTestFactory.createPlayerToSave();
        PlayerEventTestUtils.assignId(player, 4L);

        // WHEN
        boolean result = joinRequest.isFrom(player);

        // THEN
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when the join request status is expired")
    void shouldReturnTrueWhenTheJoinRequestStatusIsExpired() {
        // GIVEN
        RequestExpiration expire = () -> Arrays.stream(joinRequest.getClass().getDeclaredFields())
                .filter(field -> field.getName().equals("status"))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(joinRequest, RequestStatus.EXPIRED);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        expire.execute();

        // WHEN
        boolean result = joinRequest.isExpired();

        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should be able to accept a join request")
    void shouldAcceptAJoinRequest() {
        // GIVEN
        // WHEN
        joinRequest.accept();

        // THEN
        Assertions.assertTrue(joinRequest.isAccepted());
    }

    @Test
    @DisplayName("Should throw an error when trying to accept a non pending join request")
    void shouldThrowAnErrorWhenTryingToAcceptANonPendingJoinRequest() {
        // GIVEN
        joinRequest.accept();

        // WHEN / THEN
        Assertions.assertThrows(IllegalStateException.class, joinRequest::accept);
    }

    @Test
    @DisplayName("Should be able to reject a join request")
    void shouldRejectAJoinRequest() {
        // GIVEN
        // WHEN
        joinRequest.reject();

        // THEN
        Assertions.assertTrue(joinRequest.isRejected());
    }

    @Test
    @DisplayName("Should throw an error when trying to reject a non pending join request")
    void shouldThrowAnErrorWhenTryingToRejectANonPendingJoinRequest() {
        // GIVEN
        joinRequest.reject();

        // WHEN / THEN
        Assertions.assertThrows(IllegalStateException.class, joinRequest::reject);
    }

    @Test
    @DisplayName("Should throw an error when tries to create a join request by the event admin")
    void shouldThrowAnErrorWhenTriesToCreateAJoinRequestByTheEventAdmin() {
        // GIVEN
        Event eventToJoin = PlayerEventTestFactory.createEvent();

        // WHEN / THEN
        Assertions.assertThrows(IllegalStateException.class, () -> new JoinRequest(eventToJoin.getAdmin(), eventToJoin));
    }

    @Test
    @DisplayName("Should throw an error when tries to create a join request by a player that is already a participant")
    void shouldThrowAnErrorWhenTriesToCreateAJoinRequestByAPlayerThatIsAlreadyAParticipant() {
        // GIVEN
        Event eventToJoin = PlayerEventTestFactory.createEvent();
        eventToJoin.addParticipant(participant);

        // WHEN / THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> new JoinRequest(participant, eventToJoin));
    }

    @Test
    @DisplayName("Should throw an error when tries to create a join request by a player that already sent a request")
    void shouldThrowAnErrorWhenTriesToCreateAJoinRequestByAPlayerThatAlreadySentARequest() {
        // GIVEN / WHEN / THEN
        Assertions.assertThrows(IllegalStateException.class, () -> new JoinRequest(participant, event));
    }

    @Test
    @DisplayName("Should throw an error when tries to create a join request for a full event")
    void shouldThrowAnErrorWhenTriesToCreateAJoinRequestForAFullEvent() {
        // GIVEN
        Event eventToJoin = PlayerEventTestFactory.createEvent();
        for (int i = 0; i < 10; i++) {
            eventToJoin.addParticipant(PlayerEventTestFactory.createParticipant());
        }

        // WHEN / THEN
        Assertions.assertThrows(IllegalStateException.class, () -> new JoinRequest(participant, eventToJoin));
    }
}