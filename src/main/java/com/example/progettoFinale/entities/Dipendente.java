package com.example.progettoFinale.entities;

import com.example.progettoFinale.enums.RuoloType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dipendenti")
public class Dipendente implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String nome;
    private String cognome;
    private String email;
    @Enumerated(EnumType.STRING)
    private RuoloType ruolo;
    private Double stipendio;
    private String username;
    @JsonIgnore
    private String password;
    private String avatar;
    @Column(name = "data_assunzione")
    private LocalDate dataAssunzione;

    @OneToMany(mappedBy = "dipendente", fetch = FetchType.EAGER)
    private List<Presenza> presenze;
    @OneToMany(mappedBy = "dipendente", fetch = FetchType.EAGER)
    private List<Ferie> ferie;
    @OneToMany(mappedBy = "dipendente", fetch = FetchType.EAGER)
    private List<BustaPaga> bustepaga;
    @OneToMany(mappedBy = "dipendente", fetch = FetchType.EAGER)
    private List<Assenza> assenze;

    public Dipendente(String nome, String cognome, String email, String username, Double stipendio, String password, LocalDate dataAssunzione) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
        this.stipendio = stipendio;
        this.password = password;
        this.dataAssunzione = dataAssunzione;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }
}
