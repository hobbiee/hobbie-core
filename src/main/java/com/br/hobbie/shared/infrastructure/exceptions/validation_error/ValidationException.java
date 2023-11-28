package com.br.hobbie.shared.infrastructure.exceptions.validation_error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public record ValidationException(
        String title,
        String fields,
        int status,
        Instant timestamp,
        String details,
        String[] errors
) {
}
