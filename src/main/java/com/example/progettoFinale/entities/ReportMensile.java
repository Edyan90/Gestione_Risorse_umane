package com.example.progettoFinale.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReportMensile {
    private UUID dipendenteId;
    private String nomeCompletoDipendente;
    private String mese;
    private Long giorniLavorati;
    private Long giorniAssenti;
    private Long ferieRichieste;

    public ReportMensile(UUID dipendenteId, String nomeDipendente, String mese, Long giorniLavorati, Long giorniAssenti, Long ferieRichieste) {
        this.dipendenteId = dipendenteId;
        this.nomeCompletoDipendente = nomeDipendente;
        this.mese = mese;
        this.giorniLavorati = giorniLavorati;
        this.giorniAssenti = giorniAssenti;
        this.ferieRichieste = ferieRichieste;
    }

}
