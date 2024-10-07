package com.example.progettoFinale.services;

import com.example.progettoFinale.entities.Assenza;
import com.example.progettoFinale.exceptions.NotFoundEx;
import com.example.progettoFinale.repositories.AssenzeRepository;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AssenzeService {
    @Autowired
    private AssenzeRepository assenzeRepository;
    @Autowired
    private DipendentiService dipendentiService;

    public Assenza findByID(UUID assenzaID){
        return this.assenzeRepository.findById(assenzaID).orElseThrow(()->new NotFoundEx(assenzaID));
    }

    public Assenza saveAssenza()
}
