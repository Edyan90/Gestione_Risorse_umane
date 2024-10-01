package com.example.progettoFinale.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "buste_paga")
public class BustaPaga {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    @JsonIgnore
    private Dipendente dipendente;
    private LocalDate data;
    @Column(name = "importo_totale")
    private Double importoTotale;
    @Column(name = "ore_lavorate_extra")
    private Integer oreLavorateExtra;

    public BustaPaga(Dipendente dipendente, LocalDate data, Double importoTotale, Integer oreLavorate) {
        this.dipendente = dipendente;
        this.data = data;
        this.importoTotale = importoTotale;
        this.oreLavorateExtra = oreLavorate;
    }
}
