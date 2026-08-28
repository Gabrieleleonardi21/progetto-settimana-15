package com.example.progettosettimana15.controllers;

import com.example.progettosettimana15.entities.Utente;
import com.example.progettosettimana15.payload.UtentePayload;
import com.example.progettosettimana15.payload.UtenteResponse;
import com.example.progettosettimana15.services.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService){
        this.utenteService = utenteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UtenteResponse registra(@RequestBody @Validated UtentePayload payload){
        return UtenteResponse.from(utenteService.registra(payload));
    }

    @GetMapping("/me")
    public UtenteResponse getMe(@AuthenticationPrincipal Utente utenteCorrente) {
        return UtenteResponse.from(utenteCorrente);
    }




}
