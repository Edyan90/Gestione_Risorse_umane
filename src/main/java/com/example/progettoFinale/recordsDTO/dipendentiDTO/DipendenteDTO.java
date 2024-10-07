package com.example.progettoFinale.recordsDTO.dipendentiDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DipendenteDTO(
        @NotEmpty(message = "Il nome è obbligatorio!")
        @Size(min = 3, max = 30, message = "Il nome deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String nome,
        @NotEmpty(message = "Il cognome è obbligatorio!")
        @Size(min = 3, max = 30, message = "Il cognome deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String cognome,
        @Email
        @NotEmpty(message = "l'email è obbligatorio!")
        @Size(min = 3, max = 30, message = "l'email deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String email,
        @NotEmpty(message = "L'username username è obbligatorio!")
        @Size(min = 3, max = 30, message = "Il cognome deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String username,
        @NotEmpty(message = "La password è obbligatoria!")
        @Size(min = 3, max = 30, message = "La password deve avere un minimo di 2 ad un massimo di 20 caratteri")
        String password,
        @NotEmpty(message = "Lo stipendio è obbligatoria!")
        String stipendio,
        @NotEmpty(message = "il ruolo è obbligatorio!")
        @Size(min = 3, max = 30, message = "i ruoli disponibili sono DIPENDENTE,MANAGER")
        String ruolo,
        @NotEmpty(message = "la data di assunzion è obbligatoria")
        LocalDate dataAssunzione
) {
}
