package com.br.hobbie.modules.event.infrastructure.http.dtos.response;

import com.br.hobbie.modules.event.application.DistanceCalculator;
import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;

import java.util.Arrays;
import java.util.Objects;

public record EventResponse(
        String name,
        String description,
        String date,
        String startTime,
        String endTime,

        int capacity,

        Float latitude,
        Float longitude,

        int numberOfParticipants,

        String thumbnail,


        boolean active,

        String adminName,

        String adminAvatar,

        String[] categories,
        Float distance
) {

    public static EventResponse from(Event event, Player player, DistanceCalculator distanceCalculator) {
        var distance = distanceCalculator.getDistance(player.getMatchLatitude(), player.getMatchLongitude(), event.getLatitude(), event.getLongitude());
        return new EventResponse(
                event.getName(),
                event.getDescription(),
                event.getFormattedDate(),
                event.getFormattedStartTime(),
                event.getFormattedEndTime(),
                event.getCapacity(),
                event.getLatitude(),
                event.getLongitude(),
                event.getAmountOfParticipants(),
                event.getThumbnail(),
                event.isActive(),
                event.getAdminName(),
                event.getAdminAvatar(),
                event.getCategoriesNames(),
                (float) (Math.ceil(distance * 100) / 100f) // round to 2 decimal places
        );
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof EventResponse that)) return false;
        return capacity == that.capacity && numberOfParticipants == that.numberOfParticipants && active == that.active && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(date, that.date) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude) && Objects.equals(thumbnail, that.thumbnail) && Objects.equals(adminName, that.adminName) && Objects.equals(adminAvatar, that.adminAvatar) && Arrays.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, description, date, startTime, endTime, capacity, latitude, longitude, numberOfParticipants, thumbnail, active, adminName, adminAvatar);
        result = 31 * result + Arrays.hashCode(categories);
        return result;
    }

    @Override
    public String toString() {
        return "EventResponse{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", capacity=" + capacity +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", numberOfParticipants=" + numberOfParticipants +
                ", thumbnail='" + thumbnail + '\'' +
                ", active=" + active +
                ", adminName='" + adminName + '\'' +
                ", adminAvatar='" + adminAvatar + '\'' +
                ", categories=" + Arrays.toString(categories) +
                '}';
    }
}
