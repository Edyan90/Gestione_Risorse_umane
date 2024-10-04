package com.example.progettoFinale.recordsDTO;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record BustaPagaDTO(
        @NotBlank(message = "Il campo dipendenteID non può essere vuoto.")
        String dipendenteID,

        @NotNull(message = "La data non può essere nulla.")
        @PastOrPresent(message = "La data deve essere nel passato o il giorno corrente.")
        LocalDate data,

        @NotNull(message = "L'importo totale non può essere nullo.")
        @Positive(message = "L'importo totale deve essere positivo.")
        Double importoTotale,

        @NotNull(message = "Le ore di straordinario non possono essere nulle (se non fatte inserire '0').")
        @Min(value = 0, message = "Le ore di straordinario non possono essere negative.")
        Integer oreStraordinario
) {
}
