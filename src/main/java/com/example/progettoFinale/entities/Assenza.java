package com.example.progettoFinale.entities;

import com.example.progettoFinale.enums.StatoAssenza;
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
@Table(name = "assenze")
public class Assenza {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    @JsonIgnore
    private Dipendente dipendente;

    private LocalDate data;
    private String motivo;
    @Enumerated(EnumType.STRING)
    private StatoAssenza stato;

    public Assenza(Dipendente dipendente, LocalDate data, String motivo) {
        this.dipendente = dipendente;
        this.data = data;
        this.motivo = motivo;
        this.stato = StatoAssenza.IN_ATTESA;
    }
}
