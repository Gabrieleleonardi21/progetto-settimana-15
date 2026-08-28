package com.example.progettosettimana15.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "utente")
// costruttore vuoto riservato a JPA: protected così non può essere chiamato a mano da fuori il package
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class Utente {

    @Id
    // con Hibernate 6+, se il campo è di tipo UUID viene generato automaticamente un UUID random
    @GeneratedValue
    @Setter(AccessLevel.NONE) // l'id è generato da JPA e non deve mai essere riassegnabile dall'esterno
    private UUID id;

    @Column(nullable = false, unique = true, length = 50) // "un Utente è identificato da uno username univoco"
    private String username;

    @Column(name = "nome_completo", nullable = false, length = 75)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 75)
    private String email;

    @Column(nullable = false)
    @JsonIgnore // la password non deve MAI far parte di un JSON
    @ToString.Exclude // ...e nemmeno finire nei log tramite toString()
    private String password;

    @Enumerated(EnumType.STRING) // salva "MEMBER"/"MODERATOR" e non l'indice numerico dell'enum
    @Column(nullable = false)
    private Ruolo ruolo;

    // il ruolo non è un parametro del costruttore: ogni nuovo iscritto parte sempre da
    // MEMBER, così nessuno può registrarsi direttamente come moderatore
    public Utente(String username, String nomeCompleto, String email, String password) {
        this.username = username;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.password = password;
        this.ruolo = Ruolo.MEMBER;
    }
}
