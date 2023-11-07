package com.br.hobbie.shared.core.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = FileSizeValidator.class)
public @interface FileSize {

    String message() default "Invalid file size";

    long max();

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
