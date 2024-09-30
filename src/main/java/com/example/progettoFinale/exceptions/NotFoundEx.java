package com.example.progettoFinale.exceptions;

import java.util.UUID;

public class NotFoundEx extends RuntimeException {
    public NotFoundEx(UUID id) {
        super("L'entità con id " + id + " non è stata trovata!");
    }

    public NotFoundEx(String username) {
        super("L'entità con l'username " + username + " non è stata trovata!");
    }
}


