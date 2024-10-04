package com.example.progettoFinale.controllers;


import com.example.progettoFinale.entities.BustaPaga;
import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.recordsDTO.BustaPagaDTO;
import com.example.progettoFinale.recordsDTO.BustaPagaRespDTO;
import com.example.progettoFinale.services.BustePagheService;
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
@RequestMapping("/bustepaga")
public class BustePagheController {
    @Autowired
    private BustePagheService bustePagheService;

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public Page<BustaPaga> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy) {
        return this.bustePagheService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BustaPagaRespDTO createBustaPaga(@RequestBody @Validated BustaPagaDTO bustaPagaDTO,
                                            @AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        BustaPaga bustaPaga = this.bustePagheService.saveBustaPaga(bustaPagaDTO, dipendenteAutenticato);
        return new BustaPagaRespDTO("Bustapaga creata!", bustaPaga.getId());
    }

    @GetMapping("/singola/{bustaID}")
    public BustaPaga getBustapaga(@PathVariable UUID bustaID,
                                  @AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        BustaPaga bustaPaga = this.bustePagheService.findByID(bustaID);
        return bustaPaga;
    }

    @DeleteMapping("/singola/{bustaID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public void findAndDelete(@PathVariable UUID bustaID,
                              @AuthenticationPrincipal Dipendente dipendente) {
        this.bustePagheService.findAndDelete(bustaID, dipendente);
    }

    @PutMapping("/{bustaID}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public BustaPagaRespDTO editBustaPaga(@PathVariable UUID bustaID,
                                          @RequestBody @Validated BustaPagaDTO bustaPagaDTO,
                                          @AuthenticationPrincipal Dipendente dipendente) {
        this.bustePagheService.updateBustaPaga(bustaID, bustaPagaDTO, dipendente);
        return new BustaPagaRespDTO("busta paga modificata!", bustaID);
    }

    @GetMapping("/me")
    public List<BustaPaga> bustepagaDipendente(@AuthenticationPrincipal Dipendente dipendente) {
        return this.bustePagheService.bustepagaDipendente(dipendente.getId(), dipendente);
    }

    @GetMapping("/{dipendenteID}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public List<BustaPaga> bustepagaDipendenteforManager(@PathVariable UUID dipendenteID,
                                                         @AuthenticationPrincipal Dipendente dipendente) {
        return this.bustePagheService.bustepagaDipendente(dipendenteID, dipendente);
    }

    @GetMapping("/search")///search?mese=11&anno=2023
    public List<BustaPaga> findbyMeseAndAnno(@AuthenticationPrincipal Dipendente dipendente,
                                             @RequestParam int mese,
                                             @RequestParam int anno) {
        return this.bustePagheService.findByMeseEAnno(dipendente.getId(), mese, anno, dipendente);
    }

    @GetMapping("/{dipendenteID}/search")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public List<BustaPaga> findByDipendenteMeseAnno(@AuthenticationPrincipal Dipendente dipendente,
                                                    @RequestParam int mese,
                                                    @RequestParam int anno,
                                                    @PathVariable UUID dipendenteID) {
        return this.bustePagheService.findByMeseEAnno(dipendenteID, mese, anno, dipendente);
    }

    @GetMapping("/{dipendenteID}/importo-annuale")//fdc44cef-9309-40f2-b237-c1acfac2c8d3/ore-straordinario?anno=2023
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public Double importoAnnualeDipendente(@PathVariable UUID dipendenteID, @RequestParam int anno) {
        return this.bustePagheService.calcolaImportoTotale(dipendenteID, anno);
    }

    @GetMapping("/{dipendenteID}/ore-straordinario")//fdc44cef-9309-40f2-b237-c1acfac2c8d3/ore-straordinario?anno=2023
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public Integer importoOreStraordinario(@PathVariable UUID dipendenteID,
                                           @RequestParam int anno) {
        return this.bustePagheService.calcolaOreStraordinarioAnnuali(dipendenteID, anno);
    }
}
