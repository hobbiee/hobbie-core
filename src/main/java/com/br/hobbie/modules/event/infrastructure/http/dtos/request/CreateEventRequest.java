package com.br.hobbie.modules.event.infrastructure.http.dtos.request;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.errors.Either;
import com.br.hobbie.shared.core.ports.ExistentTagsResolver;
import com.br.hobbie.shared.core.validators.DatesMatch;
import com.br.hobbie.shared.core.validators.MaxDate;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.LongFunction;

@Getter
@DatesMatch(startDate = "startDate", endDate = "endDate")
public class CreateEventRequest implements ZonedDateTimeRequest {
    @NotBlank
    private String name;
    private String description;

    @Positive
    @Min(value = 2)
    @NotNull
    private Integer capacity;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    @FutureOrPresent
    @MaxDate(days = 7, message = "Date must be less than 7 days from now")
    private ZonedDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    @FutureOrPresent
    private ZonedDateTime endDate;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Float latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Float longitude;

    @NotEmpty
    @Size(min = 1)
    private String[] categories;

    @NotNull
    @Positive
    private Long adminId;

    public Either<RuntimeException, Event> toEntity(LongFunction<Optional<Player>> playerFinder, ExistentTagsResolver resolver) {
        var playerOptional = playerFinder.apply(adminId);

        if (playerOptional.isEmpty()) {
            return Either.left(new RuntimeException("Invalid player"));
        }

        var tags = resolver.resolve(Arrays.stream(categories)
                .map(String::toUpperCase)
                .map(String::trim)
                .map(Tag::new)
                .toArray(Tag[]::new));


        var eventCreated = new Event(name, description, capacity, startDate, endDate, latitude, longitude, tags, playerOptional.get());

        return Either.right(eventCreated);
    }
}
