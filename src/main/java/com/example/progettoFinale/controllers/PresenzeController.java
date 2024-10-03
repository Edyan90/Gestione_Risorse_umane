package com.example.progettoFinale.controllers;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.entities.Presenza;
import com.example.progettoFinale.recordsDTO.PresenzaDTO;
import com.example.progettoFinale.recordsDTO.PresenzaRespDTO;
import com.example.progettoFinale.services.PresenzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/presenze")
public class PresenzeController {
    @Autowired
    private PresenzeService presenzeService;

    @GetMapping
    public Page<Presenza> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy) {
        return this.presenzeService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PresenzaRespDTO createPresenza(PresenzaDTO presenzaDTO, @AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        Presenza presenza = this.presenzeService.savePresenza(presenzaDTO, dipendenteAutenticato);
        return new PresenzaRespDTO("Presenza salvata!", presenza.getId());
    }

    
}
