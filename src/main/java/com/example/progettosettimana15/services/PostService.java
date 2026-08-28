package com.example.progettosettimana15.services;



import com.example.progettosettimana15.entities.Post;
import com.example.progettosettimana15.entities.Utente;
import com.example.progettosettimana15.exceptions.NotFoundException;
import com.example.progettosettimana15.payload.PostPayload;
import com.example.progettosettimana15.repository.MiPiaceRepository;
import com.example.progettosettimana15.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MiPiaceRepository miPiaceRepository;

    public PostService(PostRepository postRepository, MiPiaceRepository miPiaceRepository) {
        this.postRepository = postRepository;
        this.miPiaceRepository = miPiaceRepository;
    }

    // l'autore è sempre l'utente autenticato (passato dal controller), mai un id scelto dal
    // client: se fosse un campo del payload, chiunque potrebbe pubblicare a nome di un altro
    public Post create(PostPayload payload, Utente autore) {
        Post post = new Post(payload.testo(), autore);
        return postRepository.save(post);
    }

    public Post findById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post", id));
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findByAutore(UUID autoreId) {
        return postRepository.findByUtente_Id(autoreId);
    }

    public Post update(UUID id, PostPayload payload) {
        Post post = findById(id); // entità già gestita da JPA: si modifica con i setter
        post.setTesto(payload.testo());
        // autore e data non vengono mai toccati: l'autore non cambia mai dopo la creazione,
        // e la data non ha proprio il setter (@Setter(AccessLevel.NONE) sull'entità)
        return postRepository.save(post);
    }

    // il numero di like non è memorizzato sul post: è un dato calcolato al momento della lettura
    public long contaLike(Post post) {
        return miPiaceRepository.countByPost(post);
    }

    // esposto come SpEL bean per @PreAuthorize: @postService.isAutore(#id, authentication.principal)
    public boolean isAutore(UUID id, Utente utente) {
        return findById(id).getUtente().getId().equals(utente.getId());
    }
}
