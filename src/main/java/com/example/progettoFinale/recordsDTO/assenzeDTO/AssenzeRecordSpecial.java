package com.example.progettoFinale.recordsDTO.assenzeDTO;

import com.example.progettoFinale.enums.StatoAssenza;

import java.time.LocalDate;
import java.util.UUID;

public record AssenzeRecordSpecial(UUID id, LocalDate data, String motivo, StatoAssenza stato, String nome,
                                   String cognome) {
}
