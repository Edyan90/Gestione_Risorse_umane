package com.example.progettoFinale.controllers;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.entities.Presenza;
import com.example.progettoFinale.recordsDTO.PresenzaApprovazionMensileDTO;
import com.example.progettoFinale.recordsDTO.PresenzaApprovazioneDTO;
import com.example.progettoFinale.recordsDTO.PresenzaDTO;
import com.example.progettoFinale.recordsDTO.PresenzaRespDTO;
import com.example.progettoFinale.services.PresenzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public PresenzaRespDTO createPresenza(@RequestBody @Validated PresenzaDTO presenzaDTO,
                                          @AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        Presenza presenza = this.presenzeService.savePresenza(presenzaDTO, dipendenteAutenticato);
        return new PresenzaRespDTO("Presenza salvata!", presenza.getId());
    }

    @DeleteMapping("/{presenzaID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findAndDelete(@PathVariable UUID presenzaID,
                              @AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        this.presenzeService.findAndDeletePresenza(presenzaID, dipendenteAutenticato);
    }

    @PutMapping("/{presenzaID}")
    public PresenzaRespDTO editPresenza(@PathVariable UUID presenzaID,
                                        @RequestBody @Validated PresenzaDTO presenzaDTO,
                                        @AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        this.presenzeService.updatePresenza(presenzaID, presenzaDTO, dipendenteAutenticato);
        return new PresenzaRespDTO("Aggiornata presenza!", presenzaID);
    }

    @GetMapping("/me")
    public List<Presenza> presenzeDipendente(@AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        return this.presenzeService.presenzeDelDipendente(dipendenteAutenticato.getId());
    }

    @GetMapping("/me/presenza-mensile") ///me/presenza-mensile?mese=10&anno=2024
    public List<Presenza> presenzeMensili(@AuthenticationPrincipal Dipendente dipendenteAutenticato,
                                          @RequestParam int mese,
                                          @RequestParam int anno) {
        return this.presenzeService.presenzaMensile(dipendenteAutenticato, mese, anno);
    }

    @GetMapping("/{dipendenteID}/presenza-mensile")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public List<Presenza> presenzeMensileDipendente(@PathVariable UUID dipendenteID,
                                                    @RequestParam int mese,
                                                    @RequestParam int anno) {
        return this.presenzeService.presenzaMensiletraDate(dipendenteID, mese, anno);
    }

    @GetMapping("/{presenzaID}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public Presenza getPresenza(@PathVariable UUID presenzaID) {
        return this.presenzeService.findByID(presenzaID);
    }

    @PatchMapping("/{presenzaID}/status")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
//fdc44cef-9309-40f2-b237-c1acfac2c8d3/presenza-mensile?mese=10&anno=2024
    public PresenzaRespDTO approvaPresenza(@PathVariable UUID presenzaID, PresenzaApprovazioneDTO presenzaApprovazioneDTO) {
        this.presenzeService.approvaPresenza(presenzaID, presenzaApprovazioneDTO);
        return new PresenzaRespDTO("Presenza approvata", presenzaID);
    }

    @PatchMapping("/{dipendenteID}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public PresenzaRespDTO approvaPresenzeMensile(@PathVariable UUID dipendenteID,
                                                  @RequestBody @Validated PresenzaApprovazionMensileDTO presenzaApprovazionMensileDTO) {

        this.presenzeService.approvaPresenzeMensili(dipendenteID, presenzaApprovazionMensileDTO);
        return new PresenzaRespDTO("aggiornate le presenze con lo stato richiesto del dipendente con ID: " + dipendenteID, dipendenteID);
    }
}
