package com.br.hobbie.shared.core.validators;

import com.br.hobbie.modules.event.infrastructure.http.dtos.request.ZonedDateTimeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;

public class DatesMatchValidator implements ConstraintValidator<DatesMatch, ZonedDateTimeRequest> {

    @Override
    public boolean isValid(ZonedDateTimeRequest value, ConstraintValidatorContext context) {
        Assert.notNull(value, "Value must not be null");
        Assert.notNull(value.getStartDate(), "Start date must not be null");
        Assert.notNull(value.getEndDate(), "End date must not be null");

        ZonedDateTime startDate = value.getStartDate();
        ZonedDateTime endDate = value.getEndDate();
        return startDate.isBefore(endDate);
    }
}
