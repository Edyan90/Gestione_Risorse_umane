package com.example.progettoFinale.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "presenze")
public class Presenza {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;

    private LocalDate data;
    private Boolean presente;

    public Presenza(Dipendente dipendente, LocalDate data, Boolean presente) {
        this.dipendente = dipendente;
        this.data = data;
        this.presente = presente;
    }
}
