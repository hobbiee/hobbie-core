package com.br.hobbie.shared.core.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxDateValidator.class)
public @interface MaxDate {
    int days() default 30;

    String message() default "Date must be less than 30 days from now";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
