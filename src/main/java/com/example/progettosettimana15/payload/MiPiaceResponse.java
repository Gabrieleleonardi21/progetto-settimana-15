package com.example.progettosettimana15.payload;


import com.example.progettosettimana15.entities.MiPiace;

import java.util.UUID;

public record MiPiaceResponse(
        UUID id,
        UUID postId,
        UUID utenteId,
        String utenteUsername,
        long numeroLikeDelPost
) {
    public static MiPiaceResponse from(MiPiace miPiace, long numeroLikeDelPost) {
        return new MiPiaceResponse(
                miPiace.getId(),
                miPiace.getPost().getId(),
                miPiace.getUtente().getId(),
                miPiace.getUtente().getUsername(),
                numeroLikeDelPost
        );
    }


}
