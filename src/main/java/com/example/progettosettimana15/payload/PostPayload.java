package com.example.progettosettimana15.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostPayload(
        @NotBlank(message = "Il testo del post è obbligatorio")
        @Size(max = 500, message = "Il testo non può superare i 500 caratteri")
        String testo
) {
}
