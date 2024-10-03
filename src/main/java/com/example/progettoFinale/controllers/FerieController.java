package com.example.progettoFinale.controllers;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.entities.Ferie;
import com.example.progettoFinale.recordsDTO.FerieApprovazioneDTO;
import com.example.progettoFinale.recordsDTO.FerieDTO;
import com.example.progettoFinale.recordsDTO.FerieListDateStatoDTO;
import com.example.progettoFinale.recordsDTO.FerieRespDTO;
import com.example.progettoFinale.services.FerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ferie")
public class FerieController {
    @Autowired
    private FerieService ferieService;

    @GetMapping
    public Page<Ferie> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataInizio") String sortBy) {
        return this.ferieService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FerieRespDTO createFerie(@RequestBody @Validated FerieDTO ferieDTO, @AuthenticationPrincipal Dipendente currentDipendente) {
        this.ferieService.saveFerie(ferieDTO, currentDipendente);
        return new FerieRespDTO(String.valueOf(currentDipendente.getId()));
    }

    @GetMapping("/me")
    public List<Ferie> getFerie(@AuthenticationPrincipal Dipendente currenteAuthenticationDipendente) {
        return this.ferieService.ferieDelDipendente(currenteAuthenticationDipendente.getId());
    }

    @PutMapping("/{ferieID}/stato-ferie")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public FerieRespDTO editFerieStatus(@PathVariable UUID ferieID, @RequestBody @Validated FerieApprovazioneDTO ferieApprovazioneDTO) {
        this.ferieService.approvazioneFerie(ferieID, ferieApprovazioneDTO);
        return new FerieRespDTO(String.valueOf(ferieID));
    }

    @GetMapping("/stato-ferie") // esempio ->stato-ferie?approvazione=APPROVATO
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public List<Ferie> listaFerieStato(@RequestParam String approvazione) {
        return this.ferieService.ferieStatoList(approvazione);
    }

    @GetMapping("/stato-ferie/approvati")
    //esempio /stato-ferie/approvati?dataInizio=2024-01-01&dataFine=2024-01-31&stato=approvato
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public List<Ferie> listaFerieApprovate(@RequestParam LocalDate dataInizio,
                                           @RequestParam LocalDate dataFine,
                                           @RequestParam String stato) {
        FerieListDateStatoDTO ferieListDateStatoDTO = new FerieListDateStatoDTO(dataInizio, dataFine, stato);
        return this.ferieService.ferieStatoTraDate(ferieListDateStatoDTO);
    }

    @PutMapping("/{ferieID}")
    @ResponseStatus(HttpStatus.OK)
    public FerieRespDTO editFerie(@PathVariable UUID ferieID, @RequestBody @Validated FerieDTO ferieDTO, @AuthenticationPrincipal Dipendente dipendente) {
        this.ferieService.updateFerie(ferieID, ferieDTO, dipendente);
        return new FerieRespDTO(String.valueOf(dipendente.getId()));
    }

    @DeleteMapping("/{ferieID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFerie(@PathVariable UUID ferieID, @AuthenticationPrincipal Dipendente currentDipendente) {
        this.ferieService.findAndDeleteFerie(ferieID, currentDipendente);
    }

    @GetMapping("/storico")
    public List<Ferie> storicoFerieDipendente(@AuthenticationPrincipal Dipendente currentDipendente) {
        return this.ferieService.getStorico(currentDipendente);
    }

    @GetMapping("/{ferieID}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public Ferie findByID(@PathVariable UUID ferieID) {
        return this.ferieService.findByID(ferieID);
    }

}
