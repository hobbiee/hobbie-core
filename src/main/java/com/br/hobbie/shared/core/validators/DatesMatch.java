package com.br.hobbie.shared.core.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DatesMatchValidator.class)
public @interface DatesMatch {

    String message() default "Start date must be before end date";

    String startDate();

    String endDate();

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
