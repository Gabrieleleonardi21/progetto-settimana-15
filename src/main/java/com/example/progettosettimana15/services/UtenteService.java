package com.example.progettosettimana15.services;

import com.example.progettosettimana15.entities.Ruolo;
import com.example.progettosettimana15.entities.Utente;
import com.example.progettosettimana15.exceptions.NotFoundException;
import com.example.progettosettimana15.payload.UtentePayload;
import com.example.progettosettimana15.repository.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordencoder;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordencoder){
        this.utenteRepository = utenteRepository;
        this.passwordencoder = passwordencoder;
    }

    // Registrazione pubblica: il primo utente diventa moderatore
    public Utente registra(UtentePayload payload) {
        Utente utente = new Utente(
                payload.username(),
                payload.nomeCompleto(),
                payload.email(),
                passwordencoder.encode(payload.password())
        );
        // Se è il primo utente, diventa moderatore
        if (utenteRepository.count() == 0) {
            utente.setRuolo(Ruolo.MODERATOR);
        }
        return utenteRepository.save(utente);
    }

    // Promozione: solo un moderatore (controllo nel controller) può far salire un MEMBER a MODERATOR
    public Utente promuoviAModeratore(UUID utenteId) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException("Utente", utenteId));
        utente.setRuolo(Ruolo.MODERATOR);
        return utenteRepository.save(utente);
    }
}
