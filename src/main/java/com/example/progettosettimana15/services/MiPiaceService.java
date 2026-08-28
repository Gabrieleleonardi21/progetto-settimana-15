package com.example.progettosettimana15.services;

import com.example.progettosettimana15.entities.MiPiace;
import com.example.progettosettimana15.entities.Post;
import com.example.progettosettimana15.entities.Utente;
import com.example.progettosettimana15.exceptions.LikeDuplicatoException;
import com.example.progettosettimana15.exceptions.NotFoundException;
import com.example.progettosettimana15.repository.MiPiaceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;



    @Service
    public class MiPiaceService {

        private final MiPiaceRepository miPiaceRepository;
        private final PostService postService;

        public MiPiaceService(MiPiaceRepository miPiaceRepository, PostService postService) {
            this.miPiaceRepository = miPiaceRepository;
            this.postService = postService;
        }

        // L'utente non è un parametro scelto dal client: è sempre quello autenticato.
        // È questa la ragione per cui la rotta è POST /api/posts/{id}/like e non
        // POST /api/likes con utenteId nel body: così non si può mettere like a nome di altri
        public MiPiace aggiungiLike(UUID postId, Utente utente) {
            Post post = postService.findById(postId); // 404 se il post non esiste

            // il vincolo richiesto dalla traccia, applicato nel livello di logica applicativa
            // e non lasciato al solo database
            if (miPiaceRepository.existsByUtenteAndPost(utente, post)) {
                throw new LikeDuplicatoException(
                        "L'utente '" + utente.getUsername() + "' ha già messo like a questo post");
            }

            MiPiace nuovoLike = new MiPiace(utente, post);
            return miPiaceRepository.save(nuovoLike);
        }

        // Rimuove SOLO il like dell'utente autenticato: non riceve un id di like dal client,
        // quindi non esiste modo di togliere il like di qualcun altro
        public void rimuoviLike(UUID postId, Utente utente) {
            Post post = postService.findById(postId);

            MiPiace daRimuovere = miPiaceRepository.findByUtenteAndPost(utente, post)
                    .orElseThrow(() -> new NotFoundException(
                            "L'utente '" + utente.getUsername() + "' non ha messo like a questo post"));

            miPiaceRepository.delete(daRimuovere);
        }

        public long contaLikePerPost(Post post) {
            return miPiaceRepository.countByPost(post);
        }
    }

