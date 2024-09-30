package com.example.progettoFinale.exceptions;

import java.util.UUID;

public class NotFoundEx extends RuntimeException {
    public NotFoundEx(UUID id) {
        super("L'entità con id " + id + " non è stata trovata!");
    }

    public NotFoundEx(String email) {
        super("L'entità con l'email " + email + " non è stata trovata!");
    }
}


