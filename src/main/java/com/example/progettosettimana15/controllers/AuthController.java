package com.example.progettosettimana15.controllers;


import com.example.progettosettimana15.payload.LoginPayload;
import com.example.progettosettimana15.payload.LoginResponse;
import com.example.progettosettimana15.services.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
private final AuthService authService;

public AuthController(AuthService authService){this.authService  = authService;}

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginPayload payload){
    return new LoginResponse(authService.login(payload));
    }


}

