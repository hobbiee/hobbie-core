package com.br.hobbie.shared.infrastructure.exceptions;

import com.br.hobbie.shared.infrastructure.exceptions.validation_error.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        var messages = fieldErrors
                .stream()
                .map(FieldError::getDefaultMessage)
                .toArray();

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
}
