package com.br.hobbie.modules.player.infrastructure.http.dtos.request;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.ports.ExistentTagsResolver;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public record CreatePlayerRequest(
        @NotBlank(message = "Name is required")
        String name,
        String avatar,

        @NotNull(message = "Latitude is required")
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Long latitude,
        @NotNull(message = "Longitude is required")
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Long longitude,

        @NotNull(message = "Birth date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @NotNull(message = "Interests is required")
        @Size(min = 1, message = "Interests must have at least one item")
        String[] interests
) {


    public Player toEntity(ExistentTagsResolver resolver) {
        var player = new Player(
                name,
                avatar,
                BigDecimal.valueOf(latitude),
                BigDecimal.valueOf(longitude),
                birthDate
        );

        resolver.resolve(
                Arrays.stream(interests)
                        .map(String::toUpperCase)
                        .map(String::trim)
                        .distinct()
                        .map(Tag::new)
                        .toArray(Tag[]::new)
        ).forEach(player::addInterest);

        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreatePlayerRequest that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(avatar, that.avatar) && Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude) && Objects.equals(birthDate, that.birthDate) && Arrays.equals(interests, that.interests);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, avatar, latitude, longitude, birthDate);
        result = 31 * result + Arrays.hashCode(interests);
        return result;
    }

    @Override
    public String toString() {
        return "CreatePlayer{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", birthDate='" + birthDate + '\'' +
                ", interests=" + Arrays.toString(interests) +
                '}';
    }
}
