package com.example.progettoFinale.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BustaPaga {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;
    private LocalDate data;
    private Double importoTotale;
    private Integer oreLavorate;

    public BustaPaga(Dipendente dipendente, LocalDate data, Double importoTotale, Integer oreLavorate) {
        this.dipendente = dipendente;
        this.data = data;
        this.importoTotale = importoTotale;
        this.oreLavorate = oreLavorate;
    }
}
