package com.example.progettosettimana15.payload;

import com.example.progettosettimana15.entities.Post;

import java.time.LocalDate;
import java.util.UUID;

public record PostResponse(
        UUID id,
        String testo,
        LocalDate dataPubblicazione,
        UUID autoreId,
        String autoreUsername,
        long numeroLike
) {
    // il numero di like non è un campo del Post: è un dato calcolato al momento della
    // lettura contando le righe di mi_piace, quindi arriva da fuori come parametro
    public static PostResponse from(Post post, long numeroLike) {
        return new PostResponse(
                post.getId(),
                post.getTesto(),
                post.getDataPubblicazione(),
                post.getUtente().getId(),
                post.getUtente().getUsername(),
                numeroLike
        );
    }
}
