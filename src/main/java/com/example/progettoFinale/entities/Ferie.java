package com.example.progettoFinale.entities;

import com.example.progettoFinale.enums.StatoFerie;
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
@Table(name = "ferie")
public class Ferie {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;
    @Column(name = "data_inizio")
    private LocalDate dataInizio;
    @Column(name = "data_fine")
    private LocalDate dataFine;
    @Enumerated(EnumType.STRING)
    private StatoFerie stato;

    public Ferie(Dipendente dipendente, LocalDate dataInizio, LocalDate dataFine, StatoFerie stato) {
        this.dipendente = dipendente;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.stato = stato;
    }
}
