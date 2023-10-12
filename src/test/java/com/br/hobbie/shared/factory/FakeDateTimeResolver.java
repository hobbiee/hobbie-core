package com.br.hobbie.shared.factory;

import com.br.hobbie.shared.core.ports.DateTimeResolver;
import com.br.hobbie.shared.core.validators.ValidDateValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class is only used to test the ValidDateValidator class
 *
 * @see ValidDateValidator
 */
@Deprecated(since = "This class is only used to test the ValidDateValidator class")
@AllArgsConstructor
@Getter
public final class FakeDateTimeResolver implements DateTimeResolver {
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
}
