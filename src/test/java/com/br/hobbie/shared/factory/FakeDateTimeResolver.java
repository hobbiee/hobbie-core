package com.br.hobbie.shared.factory;

import com.br.hobbie.shared.core.ports.DateTimeResolver;
import com.br.hobbie.shared.core.validators.ValidDateValidator;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class is only used to test the ValidDateValidator class
 *
 * @see ValidDateValidator
 */

public record FakeDateTimeResolver(LocalDate date, LocalTime startTime, LocalTime endTime) implements DateTimeResolver {
    public FakeDateTimeResolver(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
