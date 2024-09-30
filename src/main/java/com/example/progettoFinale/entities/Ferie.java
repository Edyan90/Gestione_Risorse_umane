package com.example.progettoFinale.entities;

import com.example.progettoFinale.enums.StatoFerie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor

public class Ferie {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;

    private LocalDate dataInizio;
    private LocalDate dataFine;
    @Enumerated(EnumType.STRING)
    private StatoFerie stato;
}
