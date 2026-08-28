package com.example.progettosettimana15.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class Post {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, length = 500)
    private String testo;

    // la data non si cambia dall'esterno: la decide il costruttore con LocalDate.now().
    // updatable = false è la stessa regola lato database, la colonna è esclusa da ogni UPDATE
    @Setter(AccessLevel.NONE)
    @Column(name = "data_pubblicazione", nullable = false, updatable = false)
    private LocalDate dataPubblicazione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    @ToString.Exclude // evita di trascinare tutto l'autore dentro il toString del post
    private Utente utente;

    public Post(String testo, Utente utente) {
        this.testo = testo;
        this.utente = utente;
        this.dataPubblicazione = LocalDate.now();
    }
}
