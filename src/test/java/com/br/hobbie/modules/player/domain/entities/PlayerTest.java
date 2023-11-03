package com.br.hobbie.modules.player.domain.entities;

import com.br.hobbie.modules.event.application.HaversineDistanceCalculator;
import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.entities.JoinRequest;
import com.br.hobbie.shared.core.errors.Either;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import com.br.hobbie.shared.utils.PlayerEventTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    static final ZonedDateTime START_DATE = ZonedDateTime.now();
    static final ZonedDateTime END_DATE = ZonedDateTime.now().plusDays(1);
    static final Float LATITUDE = 10F;
    static final Float LONGITUDE = 10F;

    static final int CAPACITY = 10;

    static final Set<Tag> TAGS = Set.of(new Tag("tag1"), new Tag("tag2"));

    private Player player;
    private Event event;


    @BeforeEach
    void setUp() {
        player = PlayerEventTestFactory.createPlayer();
        event = new Event("name", "description", CAPACITY, START_DATE, END_DATE, LATITUDE, LONGITUDE, "thumbnail", TAGS, player);
    }

    @Test
    @DisplayName("Should add interest to player")
    void addInterest_WhenSuccessFull() {
        // GIVEN
        Tag tag = new Tag("tag");

        // WHEN
        player.addInterest(tag);

        // THEN
        Assertions.assertTrue(player.hasInterestIn(tag));
    }

    @Test
    @DisplayName("Should create an event and set it as admin event")
    void createEvent_SetAdminEvent() {
        // GIVEN - setUp

        // WHEN
        player.createEvent(event);

        // THEN
        Assertions.assertTrue(event.isOwner(player));
        Assertions.assertTrue(player.isParticipant(event));
    }

    @Test
    @DisplayName("Should return true if the players have same id")
    void should_ReturnTrue_WhenPlayersHaveSameId() {
        // GIVEN
        var player1 = PlayerEventTestFactory.createPlayer();
        var player2 = PlayerEventTestFactory.createPlayer();

        // WHEN
        boolean result = player1.isSameOf(player2);

        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false if the players have different id")
    void should_ReturnFalse_WhenPlayersHaveDifferentId() {
        // GIVEN
        var player1 = PlayerEventTestFactory.createPlayer();
        var player2 = PlayerEventTestFactory.createParticipant();


        // WHEN
        boolean result = player1.isSameOf(player2);

        // THEN
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Should send an interest request for some event if the player is not the admin")
    void sendInterestRequest_WhenSuccessFull() {
        // GIVEN
        Player participant = PlayerEventTestFactory.createParticipant();

        // WHEN
        Either<IllegalStateException, JoinRequest> result = participant.sendInterest(event);

        // THEN
        Assertions.assertTrue(result.isRight());
        Assertions.assertTrue(event.hasJoinRequestFrom(participant));
    }

    @Test
    @DisplayName("Cannot send a join request with an event that overlaps with another event")
    void cannotSend_JoinRequest_WithOverlappingEvents() {
        // GIVEN
        Player participant = PlayerEventTestFactory.createParticipant();
        PlayerEventTestFactory.createStartedEvent(participant);
        Event overlappingEvent = PlayerEventTestFactory.createStartedEvent(player);

        // WHEN
        Either<IllegalStateException, JoinRequest> result = participant.sendInterest(overlappingEvent);

        // THEN
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Player already has an event at this time", result.getLeft().getMessage());
    }

    @Test
    @DisplayName("Should accept a join request of some another player")
    void should_AcceptJoinRequest_OfSomeAnotherPlayer() {
        // GIVEN
        Player participant = PlayerEventTestFactory.createParticipant();
        participant.sendInterest(event);

        // WHEN
        Either<RuntimeException, Boolean> result = player.acceptJoinRequest(participant, event);

        // THEN
        Assertions.assertTrue(result.isRight());
        Assertions.assertEquals(2, event.getAmountOfParticipants());
    }

    @Test
    @DisplayName("Should returns an error when tries to accept a request from a player that has not sent a request")
    void should_ReturnsAnError_WhenTriesToAcceptARequestFromAPlayerThatHasNotSentARequest() {
        // GIVEN
        Player participant = PlayerEventTestFactory.createParticipant();

        // WHEN
        Either<RuntimeException, Boolean> result = player.acceptJoinRequest(participant, event);

        // THEN
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Player must have a join request", result.getLeft().getMessage());
    }

    @Test
    @DisplayName("Should returns an error when tries to accept a request when the player is not the admin of the event")
    void should_ReturnsAnError_WhenTriesToAcceptARequestWhenThePlayerIsNotTheAdminOfTheEvent() {
        // GIVEN
        Player participant = PlayerEventTestFactory.createParticipant();
        participant.sendInterest(event);


        // WHEN
        Either<RuntimeException, Boolean> result = participant.acceptJoinRequest(participant, event);

        // THEN
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Player must be the admin of the event", result.getLeft().getMessage());
    }

    @Test
    @DisplayName("Should returns an error when tries to reject a request when the player is not the admin of the event")
    void should_ReturnsAnError_WhenTriesToRejectARequestWhenThePlayerIsNotTheAdminOfTheEvent() {
        // GIVEN
        Player participant = PlayerEventTestFactory.createParticipant();
        participant.sendInterest(event);


        // WHEN
        Either<RuntimeException, Boolean> result = participant.rejectJoinRequest(participant, event);

        // THEN
        Assertions.assertTrue(result.isLeft());
        Assertions.assertEquals("Player must be the admin of the event", result.getLeft().getMessage());
    }

    @Test
    @DisplayName("Should reject a join request of some another player")
    void should_RejectJoinRequest_OfSomeAnotherPlayer() {
        // GIVEN
        Player participant = PlayerEventTestFactory.createParticipant();
        participant.sendInterest(event);

        // WHEN
        Either<RuntimeException, Boolean> result = player.rejectJoinRequest(participant, event);
        Set<JoinRequest> requests = PlayerEventTestUtils.extractSomeField(Event.class, "requests", event, Set.class);

        // THEN
        Assertions.assertEquals(1, event.getAmountOfParticipants());
        Assertions.assertTrue(requests.stream().anyMatch(request -> request.isFrom(participant)));
        Assertions.assertTrue(requests.stream().anyMatch(JoinRequest::isRejected));
        Assertions.assertTrue(result.isRight());
    }

    @Test
    @DisplayName("Should do nothing when tries to reject a request from a player that has not sent a request")
    void should_DoNothing_WhenTriesToRejectARequestFromAPlayerThatHasNotSentARequest() {
        // GIVEN
        Player participant = PlayerEventTestFactory.createParticipant();

        // WHEN
        player.rejectJoinRequest(participant, event);
        var requests = PlayerEventTestUtils.extractSomeField(Event.class, "requests", event, Set.class);

        // THEN
        Assertions.assertEquals(1, event.getAmountOfParticipants());
        Assertions.assertTrue(requests.isEmpty());
    }

    @Test
    @DisplayName("Should return true when the event distance is within the player radius")
    void should_ReturnTrue_WhenEventDistanceIsWithinPlayerRadius() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // GIVEN
        var participant = PlayerEventTestFactory.createParticipant();
        var event = PlayerEventTestFactory.createEvent();
        var participantLatitude = participant.getMatchLatitude();
        var participantLongitude = participant.getMatchLongitude();
        var eventLatitude = event.getLatitude();
        var eventLongitude = event.getLongitude();
        var distance = new HaversineDistanceCalculator().getDistance(participantLatitude, participantLongitude, eventLatitude, eventLongitude);

        // WHEN
        var result = participant.distanceIsWithinRadius(distance);


        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when the event distance is not within the player radius")
    void should_ReturnFalse_WhenEventDistanceIsNotWithinPlayerRadius() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // GIVEN
        var participant = PlayerEventTestFactory.createParticipant();
        var participantLatitude = participant.getMatchLatitude();
        var participantLongitude = participant.getMatchLongitude();
        var eventLatitude = event.getLatitude();
        var eventLongitude = event.getLongitude();
        var distance = new HaversineDistanceCalculator().getDistance(participantLatitude, participantLongitude, eventLatitude, eventLongitude);

        // WHEN
        var result = participant.distanceIsWithinRadius(distance);

        // THEN
        Assertions.assertFalse(result);
        Assertions.assertTrue(distance > participant.getRadius().doubleValue());
    }
}