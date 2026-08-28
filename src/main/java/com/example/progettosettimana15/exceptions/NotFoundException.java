package com.example.progettosettimana15.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String tipo, UUID id){ super(tipo + " con id " + " non trovato");}
}
