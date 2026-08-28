package com.example.progettosettimana15.payload;

import com.example.progettosettimana15.entities.Ruolo;
import com.example.progettosettimana15.entities.Utente;

import java.util.UUID;

// DTO di risposta: niente password, non va mai restituita al client
public record UtenteResponse(
        UUID id,
        String username,
        String nomeCompleto,
        String email,
        Ruolo ruolo
) {
    public static UtenteResponse from(Utente utente) {
        return new UtenteResponse( // cosi si converte Utente in UtenteResponse
                utente.getId(),
                utente.getUsername(),
                utente.getNomeCompleto(),
                utente.getEmail(),
                utente.getRuolo()
        );
    }
}
