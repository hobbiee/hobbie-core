package com.br.hobbie.shared.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
record AuthenticationExceptionResponse(
        String title,
        int status,
        String error,
        Instant timestamp,
        String exception
) {
}
