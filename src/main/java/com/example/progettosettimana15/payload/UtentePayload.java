package com.example.progettosettimana15.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UtentePayload(
        @NotBlank(message = "Lo username è obbligatorio")
        @Size(max = 50, message = "Lo username non può superare i 50 caratteri")
        String username,
        @NotBlank(message = "Il nome completo è obbligatorio")
        String nomeCompleto,
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è in un formato valido")
        String email,
        @NotBlank(message = "La password è obbligatoria")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "La password deve contenere almeno 8 caratteri, con almeno una lettera e un numero"
        )
        String password
) {
}
