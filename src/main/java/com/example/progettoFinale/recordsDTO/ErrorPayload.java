package com.example.progettoFinale.recordsDTO;


import java.time.LocalDateTime;

public record ErrorPayload(
        String message, LocalDateTime timestamp
) {

}
