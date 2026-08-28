package com.example.progettosettimana15.entities;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
// il vincolo UNIQUE composto traduce a livello di database la regola
// "uno stesso utente non può mettere più di un like allo stesso post"
@Table(
        name = "mi_piace",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_mi_piace_utente_post",
                columnNames = {"utente_id", "post_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class MiPiace {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public MiPiace(Utente utente, Post post) {
        this.utente = utente;
        this.post = post;
    }
}
