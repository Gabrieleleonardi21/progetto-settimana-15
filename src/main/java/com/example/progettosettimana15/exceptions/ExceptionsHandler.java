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

    // 401: token mancante, non valido o scaduto — lanciata dal JwtFilter.
    // Senza questo handler l'exceptionResolver non scriveva nulla nella risposta,
    // che partiva con lo status di default: 200 con body vuoto
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorized(UnauthorizedException e) {
        return ErrorResponse.of(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }

    // 400: like doppio sullo stesso post — lanciata da MiPiaceService
    @ExceptionHandler(LikeDuplicatoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleLikeDuplicato(LikeDuplicatoException e) {
        return ErrorResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    // 500: rete di sicurezza per qualsiasi errore non previsto,
    // così nessuna eccezione produce mai una risposta vuota con status ingannevole
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception e) {
        return ErrorResponse.of("Errore interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
