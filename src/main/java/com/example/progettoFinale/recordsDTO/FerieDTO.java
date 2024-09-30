package com.example.progettoFinale.recordsDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

public record FerieDTO (
@NotEmpty(message = "l'id del dipendente è obbligatorio!")
String dipendenteID,
@NotNull(message = "manca la data d'inizio ferie!")
LocalDate dataInizio,
@NotNull(message = "manca la data di fine ferie!")
LocalDate dataFine
){
}
