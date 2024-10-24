package com.example.progettoFinale.recordsDTO.dipendentiDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record DipendenteEditDTO(
        @NotNull(message = "ID del dipendente è obbligatorio!")
        UUID dipendenteID,

        @Size(min = 3, max = 30, message = "Il nome deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String nome,

        @Size(min = 3, max = 30, message = "Il cognome deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String cognome,
        @Email

        @Size(min = 3, max = 30, message = "l'email deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String email,

        @Size(min = 3, max = 30, message = "Il cognome deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String username,

        @Size(min = 3, max = 30, message = "La password deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String password,

        String stipendio,

        @Size(min = 3, max = 30, message = "i ruoli disponibili sono DIPENDENTE,MANAGER")
        String ruolo,

        LocalDate dataAssunzione) {
}
