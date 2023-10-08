package com.br.hobbie.modules.event.infrastructure.http.dtos.request;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.errors.Either;
import com.br.hobbie.shared.core.ports.DateTimeResolver;
import com.br.hobbie.shared.core.ports.ExistentTagsResolver;
import com.br.hobbie.shared.core.validators.MaxDate;
import com.br.hobbie.shared.core.validators.ValidDate;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.LongFunction;

@Getter
@ValidDate(fields = {"date", "startTime", "endTime"})
public class CreateEventRequest implements DateTimeResolver {
    @NotBlank
    private String name;
    private String description;

    @Positive
    @Min(value = 2)
    private Integer capacity;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    @FutureOrPresent
    @MaxDate(days = 7, message = "Date must be less than 7 days from now")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime endTime;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private BigDecimal latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private BigDecimal longitude;

    
    private String thumbnail;

    @NotEmpty
    @Size(min = 1)
    private String[] categories;

    @NotNull
    @Positive
    private Long adminId;

    public Either<RuntimeException, Event> toEntity(LongFunction<Optional<Player>> playerFinder, ExistentTagsResolver resolver) {
        var playerOptional = playerFinder.apply(adminId);
        if (playerOptional.isEmpty() || playerOptional.get().getAdminEvent() != null) {
            return Either.left(new RuntimeException("Invalid player"));
        }

        var tags = resolver.resolve(Arrays.stream(categories)
                .map(String::toUpperCase)
                .map(String::trim)
                .map(Tag::new)
                .toArray(Tag[]::new));


        var eventCreated = new Event(name, description, capacity, date, startTime, endTime, latitude, longitude, thumbnail, tags, playerOptional.get());

        return Either.right(eventCreated);
    }
}
