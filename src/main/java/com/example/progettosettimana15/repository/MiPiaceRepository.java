package com.example.progettosettimana15.repository;

import com.example.progettosettimana15.entities.MiPiace;
import com.example.progettosettimana15.entities.Post;
import com.example.progettosettimana15.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MiPiaceRepository extends JpaRepository<MiPiace, UUID> {

    // Derived query al posto del filtro con Stream: il controllo sui like duplicati
    // resta nel Service, ma la ricerca la fa il database sull'indice del vincolo UNIQUE
    boolean existsByUtenteAndPost(Utente utente, Post post);

    // serve per la rimozione: recupera il like di QUEL utente su QUEL post
    Optional<MiPiace> findByUtenteAndPost(Utente utente, Post post);

    // conteggio dei like di un post, il numero che si vede sotto ogni post di un social
    long countByPost(Post post);
}
