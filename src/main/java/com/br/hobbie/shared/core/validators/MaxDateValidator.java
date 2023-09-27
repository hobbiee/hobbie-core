package com.br.hobbie.shared.core.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.Temporal;

public class MaxDateValidator implements ConstraintValidator<MaxDate, Temporal> {

    private int days;

    @Override
    public void initialize(MaxDate constraintAnnotation) {
        days = constraintAnnotation.days();
    }

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {
        return LocalDate.now().plusDays(days).isAfter(LocalDate.from(value));
    }

}
