package com.example.progettosettimana15.services;

import com.example.progettosettimana15.entities.Utente;
import com.example.progettosettimana15.exceptions.UnauthorizedException;
import com.example.progettosettimana15.payload.LoginPayload;
import com.example.progettosettimana15.repository.UtenteRepository;
import com.example.progettosettimana15.security.JWTtools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteRepository utenteRepository;
    private final JWTtools jwtTools;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UtenteRepository utenteRepository, JWTtools jwtTools, PasswordEncoder passwordEncoder){
        this.utenteRepository = utenteRepository;
        this.jwtTools = jwtTools;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginPayload payload){
        Utente utente = utenteRepository.findByEmail(payload.email())
                .orElseThrow(() -> new UnauthorizedException("Email o password errati"));
        if (!passwordEncoder.matches(payload.password(), utente.getPassword())){
            throw new UnauthorizedException("Email o password errati");
        }
        return jwtTools.generateToken(utente);
    }





}
