package com.example.progettoFinale.recordsDTO.assenzeDTO;

import jakarta.validation.constraints.NotEmpty;

public record AssenzaApprovazioneDTO(
        @NotEmpty(message = "manca lo stato di assenza!")
        String statoAssenza) {
}
