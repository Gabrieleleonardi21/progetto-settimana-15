package com.example.progettosettimana15.exceptions;

import com.example.progettosettimana15.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        return ErrorResponse.of("La risorsa che stai cercando non esiste: " + e.getMessage(), HttpStatus.NOT_FOUND.value());
    }
}
