package com.example.progettoFinale.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorPayload { // Questa classe serve per definire come sarà fatto il payload delle risposte di errore
    private String message;
    private LocalDateTime timestamp;
}
