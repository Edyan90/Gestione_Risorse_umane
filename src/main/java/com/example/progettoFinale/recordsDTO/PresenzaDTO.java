package com.example.progettoFinale.recordsDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PresenzaDTO(
        @NotNull(message = "La data è obbligatoria in formato YYYY-MM-DD!")
        LocalDate data,
        @NotNull(message = "la presenza è obbligatoria in TRUE o FALSE")
        Boolean presente,
        @NotEmpty(message = "Il dipendenteID è obbligatorio")
        String dipendenteID
) {
}
