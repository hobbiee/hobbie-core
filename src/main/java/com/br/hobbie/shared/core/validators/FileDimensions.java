package com.br.hobbie.shared.core.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = FileDimensionsValidator.class)
public @interface FileDimensions {

    String message() default "Invalid file dimensions";

    int minWidth();

    int minHeight();

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
