package com.example.progettoFinale.recordsDTO;

import jakarta.validation.constraints.NotEmpty;

public record PresenzaApprovazioneDTO(
        @NotEmpty(message = "manca lo stato di presenza!")
        String statoPresenza
) {
}
