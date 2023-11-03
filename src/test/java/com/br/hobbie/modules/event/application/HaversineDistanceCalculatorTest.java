package com.br.hobbie.modules.event.application;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HaversineDistanceCalculatorTest {

    private static final double RADIUS = 6371;
    private static Player player = null;
    private static Event event = null;

    private static DistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        player = PlayerEventTestFactory.createParticipant();
        event = PlayerEventTestFactory.createEvent();
        calculator = new HaversineDistanceCalculator();
    }

    @Test
    @DisplayName("Should return true if the event is within the player's radius")
    void shouldReturnTrue_WhenEventIsWithinPlayerRadius() {
        // GIVEN - WHEN
        var result = calculator.isWithinRadius(player, event);

        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("should return the distance between two points on the surface of Earth based on the Haversine formula")
    void shouldReturnDistanceBetweenTwoPoints_WhenUsingHaversineFormula() {
        // GIVEN
        var playerLatitude = player.getMatchLatitude();
        var playerLongitude = player.getMatchLongitude();
        var eventLatitude = event.getLatitude();
        var eventLongitude = event.getLongitude();
        var result = calculator.getDistance(playerLatitude, playerLongitude, eventLatitude, eventLongitude);
        // WHEN
        double sin = Math.sin(Math.toRadians(eventLatitude - playerLatitude) / 2);

        double a = sin * sin
                + Math.cos(Math.toRadians(playerLatitude)) * Math.cos(Math.toRadians(eventLatitude))
                * Math.sin(Math.toRadians(eventLongitude - playerLongitude) / 2) * sin;

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        Float expected = (float) (RADIUS * c * 1000);

        // THEN
        Assertions.assertEquals(expected, result);
    }
}