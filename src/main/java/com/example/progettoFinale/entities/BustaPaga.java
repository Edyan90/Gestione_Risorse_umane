package com.example.progettoFinale.entities;

import jakarta.persistence.*;
import lombok.*;

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

    public BustaPaga(Dipendente dipendente, LocalDate data, Double importoTotale) {
        this.dipendente = dipendente;
        this.data = data;
        this.importoTotale = importoTotale;
    }
}
