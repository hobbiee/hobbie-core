package com.br.hobbie.shared.core.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = FilesAllowedValidator.class)
public @interface FilesAllowed {

    String message() default "Invalid file type";

    String[] extensions();

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
