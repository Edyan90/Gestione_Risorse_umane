package com.example.progettoFinale.recordsDTO.assenzeDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record GiustificazioneDTO(
        @NotNull(message = "La data non può essere nulla.")
        LocalDate data,
        @NotEmpty(message = "Il motivo non può essere vuoto.")
        @Size(max = 255, message = "Il motivo non può superare i 255 caratteri.")
        String motivo,
        @NotEmpty(message = "Lo stato non può essere vuoto.")
        String stato
) {
}
