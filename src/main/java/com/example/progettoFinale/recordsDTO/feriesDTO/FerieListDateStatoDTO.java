package com.example.progettoFinale.recordsDTO.feriesDTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FerieListDateStatoDTO(
        @NotNull(message = "manca la data d'inizio ferie!")
        LocalDate dataInizio,
        @NotNull(message = "manca la data di fine ferie!")
        LocalDate dataFine,
        @NotNull(message = "manca lo stato delle ferie se APPROVATO,RIFIUTATO O RICHIESTO!")
        String stato
) {
}
