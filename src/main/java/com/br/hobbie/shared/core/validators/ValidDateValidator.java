package com.br.hobbie.shared.core.validators;

import com.br.hobbie.shared.core.ports.DateTimeResolver;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidDateValidator implements ConstraintValidator<ValidDate, DateTimeResolver> {
    private String[] fields;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(DateTimeResolver object, ConstraintValidatorContext context) {
        if (object == null) {
            return false;
        }

        // if the date is today, the start time must be after now and the end time must be after the start time
        if (LocalDate.now().equals(object.getDate())) {
            return (object.getStartTime().isAfter(LocalTime.now())
                    && object.getEndTime().isAfter(object.getStartTime())) && object.getStartTime().isBefore(LocalTime.of(23, 0, 0));
        }

        // if date is in the future, both end date and start date can be after now
        // but the end date still cannot be before the start date
        return object.getEndTime().isAfter(object.getStartTime()) && object.getStartTime().isBefore(LocalTime.of(23, 0, 0));
    }
}

