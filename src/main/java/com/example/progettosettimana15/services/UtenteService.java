package com.example.progettosettimana15.services;

import com.example.progettosettimana15.entities.Utente;
import com.example.progettosettimana15.payload.UtentePayload;
import com.example.progettosettimana15.repository.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordencoder;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordencoder){
        this.utenteRepository = utenteRepository;
        this.passwordencoder = passwordencoder;
    }
    public Utente registra(UtentePayload payload) {
        Utente utente = new Utente(
                payload.username(),
                payload.nomeCompleto(),
                payload.email(),
                passwordencoder.encode(payload.password())
        );
        return utenteRepository.save(utente);
    }
}
