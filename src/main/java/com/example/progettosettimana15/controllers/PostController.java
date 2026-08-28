package com.example.progettosettimana15.controllers;

import com.example.progettosettimana15.entities.MiPiace;
import com.example.progettosettimana15.entities.Post;
import com.example.progettosettimana15.entities.Utente;
import com.example.progettosettimana15.payload.MiPiaceResponse;
import com.example.progettosettimana15.payload.PostPayload;
import com.example.progettosettimana15.payload.PostResponse;
import com.example.progettosettimana15.services.MiPiaceService;
import com.example.progettosettimana15.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final MiPiaceService miPiaceService;

    public PostController(PostService postService, MiPiaceService miPiaceService) {
        this.postService = postService;
        this.miPiaceService = miPiaceService;
    }

    // CREAZIONE: basta essere autenticati. L'autore non è nel payload, è l'utente del token
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@RequestBody @Validated PostPayload payload,
                               @AuthenticationPrincipal Utente utenteCorrente) {
        Post post = postService.create(payload, utenteCorrente);
        return PostResponse.from(post, postService.contaLike(post));
    }

    // LETTURA: basta essere autenticati, i post sono visibili a tutti gli iscritti
    @GetMapping
    public List<PostResponse> findAll() {
        return postService.findAll().stream()
                .map(post -> PostResponse.from(post, postService.contaLike(post)))
                .toList();
    }

    @GetMapping("/{id}")
    public PostResponse findById(@PathVariable UUID id) {
        Post post = postService.findById(id);
        return PostResponse.from(post, postService.contaLike(post));
    }

    // tutti i post di un determinato autore
    @GetMapping("/autore/{autoreId}")
    public List<PostResponse> findByAutore(@PathVariable UUID autoreId) {
        return postService.findByAutore(autoreId).stream()
                .map(post -> PostResponse.from(post, postService.contaLike(post)))
                .toList();
    }

    // MODIFICA: autorizzazione basata sulla PROPRIETÀ della risorsa, non sul ruolo.
    // Passa chi è l'autore di quel post, oppure un moderatore in funzione di controllo
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or @postService.isAutore(#id, authentication.principal)")
    public PostResponse update(@PathVariable UUID id, @RequestBody @Validated PostPayload payload) {
        Post post = postService.update(id, payload);
        return PostResponse.from(post, postService.contaLike(post));
    }

    // LIKE: nessuna regola oltre all'autenticazione, ma l'utente è preso dal token.
    // Il vincolo sui like duplicati è applicato dentro MiPiaceService
    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public MiPiaceResponse aggiungiLike(@PathVariable UUID id,
                                        @AuthenticationPrincipal Utente utenteCorrente) {
        MiPiace miPiace = miPiaceService.aggiungiLike(id, utenteCorrente);
        return MiPiaceResponse.from(miPiace, miPiaceService.contaLikePerPost(miPiace.getPost()));
    }

    // RIMOZIONE LIKE: la proprietà è garantita dalla forma stessa della rotta,
    // non ricevendo un id di like non si può togliere quello di un altro utente
    @DeleteMapping("/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rimuoviLike(@PathVariable UUID id,
                            @AuthenticationPrincipal Utente utenteCorrente) {
        miPiaceService.rimuoviLike(id, utenteCorrente);
    }
}

