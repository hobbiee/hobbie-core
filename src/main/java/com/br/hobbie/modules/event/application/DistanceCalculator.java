package com.br.hobbie.modules.event.application;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public interface DistanceCalculator {

    double EARTH_RADIUS = 6371;

    boolean isWithinRadius(@NotNull Player player, @NotNull Event event);

    /**
     * <h2>
     * Calculates the distance between two points on the surface of Earth
     * </h2>
     *
     * <h3>
     * Default implementation of the Haversine formula
     * </h3>
     *
     * <p>
     * This method can be easily override to use a different formula by others implementations of the interface
     * </p>
     *
     * <p>
     * By default, the formula used is the Haversine formula, let's see how it works:
     * </p>
     *
     * <ul>
     *     <li>First, we need to convert the latitude and longitude of the two points to radians</li>
     *     <li>Then, we calculate the difference between the two latitudes and longitudes</li>
     *     <li>After that, we calculate the square of half the chord length between the two points</li>
     *     <li>Finally, we calculate the distance between the two points</li>
     *     <li>And we return the distance</li>
     *     <li>Easy, right? :)</li>
     * </ul>
     *
     * <p>
     *     Another possible implementation of this interface could be the Vincenty formula or even some Google Maps API.
     * </p>
     *
     * @param playerLatitude  - latitude of the player
     * @param playerLongitude - longitude of the player
     * @param eventLatitude   - latitude of the event
     * @param eventLongitude  - longitude of the event
     * @return the distance between the two points in meters
     */

    default Float getDistance(float playerLatitude, float playerLongitude, float eventLatitude, float eventLongitude) {
        double latitudeDistance = Math.toRadians(playerLatitude - eventLatitude);
        double longitudeDistance = Math.toRadians(playerLongitude - eventLongitude);

        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(playerLatitude)) * Math.cos(Math.toRadians(eventLatitude))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (float) (EARTH_RADIUS * c * 1000);
    }
}
