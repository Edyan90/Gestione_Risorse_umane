package com.example.progettoFinale.controllers;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.entities.Ferie;
import com.example.progettoFinale.recordsDTO.FerieDTO;
import com.example.progettoFinale.recordsDTO.FerieRespDTO;
import com.example.progettoFinale.services.FerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ferie")
public class FerieController {
    @Autowired
    private FerieService ferieService;

    @GetMapping
    public Page<Ferie> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy) {
        return this.ferieService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FerieRespDTO createFerie(@RequestBody @Validated FerieDTO ferieDTO) {
        return new FerieRespDTO(String.valueOf(this.ferieService.saveFerie(ferieDTO).getId()));
    }

    @GetMapping("/me")
    public List<Ferie> getFerie(@AuthenticationPrincipal Dipendente currenteAuthenticationDipendente) {
        return this.ferieService.ferieDelDipendente(currenteAuthenticationDipendente.getId());
    }


}
