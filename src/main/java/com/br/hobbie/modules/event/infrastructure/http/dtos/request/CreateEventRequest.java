package com.br.hobbie.modules.event.infrastructure.http.dtos.request;

import com.br.hobbie.shared.core.validations.BeforeThan;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class CreateEventRequest {
    @NotBlank
    private String name;
    private String description;

    @Positive
    @Size(min = 2)
    private int capacity;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    @PastOrPresent
    private LocalDate date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    @FutureOrPresent
    private LocalTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    @Future
    @BeforeThan(field = "endTime", value = "startTime")
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
}
