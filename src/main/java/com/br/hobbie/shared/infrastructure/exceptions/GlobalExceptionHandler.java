package com.br.hobbie.shared.infrastructure.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        var fieldErrors = ex.getBindingResult().getFieldErrors();
        var fields = fieldErrors
                .stream()
                .map(FieldError::getField)
                .distinct()
                .collect(Collectors.joining(", "));


        var exception = new ValidationException(
                "Bad Request Exception. Invalid fields. Check the api docs.",
                fields,
                status.value(),
                Instant.now(),
                ex.getClass().getName(),
                fieldErrors
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toArray(String[]::new)
        );

        return ResponseEntity.badRequest().body(exception);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<AuthenticationExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        var exception = new AuthenticationExceptionResponse(
                "Sorry we could not authnticate you. Check your credentials and try again.",
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                Instant.now(),
                ex.getClass().getName()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception);
    }
}
