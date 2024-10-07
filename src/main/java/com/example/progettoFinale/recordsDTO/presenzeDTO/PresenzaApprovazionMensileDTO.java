package com.example.progettoFinale.recordsDTO.presenzeDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PresenzaApprovazionMensileDTO(@NotNull(message = "Manca il mese in formato numero ovvero da 1 a 12")
                                            Integer mese,
                                            @NotNull(message = "Manca l'anno!, non puoi inserire anni prima del 2020 e dopo l'anno odierno")
                                            Integer anno,
                                            @NotEmpty(message = "manca lo stato di presenza!")
                                            String statoPresenza) {
}
