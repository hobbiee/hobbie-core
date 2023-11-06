package com.br.hobbie.modules.player.infrastructure.http.dtos.request;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.ports.ExistentTagsResolver;
import com.br.hobbie.shared.core.ports.FileUploader;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public record CreatePlayerRequest(
        @NotBlank(message = "Name is required")
        String name,
        MultipartFile avatar,
        @NotNull(message = "Latitude is required")
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Float latitude,
        @NotNull(message = "Longitude is required")
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Float longitude,

        @NotNull
        @Range(min = 250, max = 15000, message = "Radius must be between 250 and 15000 meters") // 15km
        BigDecimal radius,

        @NotNull(message = "Birth date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @NotNull(message = "Interests is required")
        @Size(min = 1, message = "Interests must have at least one item")
        String[] interests
) {


    public Player toEntity(ExistentTagsResolver resolver, FileUploader uploader) {
        var avatarLink = Optional.ofNullable(avatar)
                .map(uploader::uploadFile)
                .orElse("");

        var player = new Player(
                name,
                avatarLink,
                latitude,
                longitude,
                radius,
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
