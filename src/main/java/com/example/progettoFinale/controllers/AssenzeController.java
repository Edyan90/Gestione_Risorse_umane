package com.example.progettoFinale.controllers;

import com.example.progettoFinale.entities.Assenza;
import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.recordsDTO.assenzeDTO.AssenzaApprovazioneDTO;
import com.example.progettoFinale.recordsDTO.assenzeDTO.AssenzaDipendenteDTO;
import com.example.progettoFinale.recordsDTO.assenzeDTO.AssenzaRespDTO;
import com.example.progettoFinale.recordsDTO.assenzeDTO.GiustificazioneDTO;
import com.example.progettoFinale.services.AssenzeService;
import com.example.progettoFinale.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assenze")
public class AssenzeController {
    @Autowired
    private AssenzeService assenzeService;
    @Autowired
    private DipendentiService dipendentiService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssenzaRespDTO createAssenza(@RequestBody @Validated GiustificazioneDTO giustificazioneDTO,
                                        @AuthenticationPrincipal Dipendente dipendente) {
        Assenza assenza = this.assenzeService.saveGiustificazione(giustificazioneDTO, dipendente);
        return new AssenzaRespDTO("Assenza creata!", assenza.getId());
    }

    @DeleteMapping("/{assenzaID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findAndDelete(@PathVariable UUID assenzaID, @AuthenticationPrincipal Dipendente dipendente) {
        this.assenzeService.findAndDeleteAssenza(assenzaID, dipendente);
    }

    @PostMapping("/assenza-manager")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public AssenzaRespDTO createAssenzaFromMAnager(@RequestBody @Validated AssenzaDipendenteDTO assenzaDTO) {
        Assenza assenza = this.assenzeService.saveAssenzaFromManager(assenzaDTO);
        return new AssenzaRespDTO("assenza del dipendete creata!", assenzaDTO.dipendenteID());
    }

    @PutMapping("/{assenzaID}")
    @ResponseStatus(HttpStatus.OK)
    public AssenzaRespDTO editAssenza(@PathVariable UUID assenzaID,
                                      @RequestBody @Validated AssenzaDipendenteDTO assenzaDTO,
                                      @AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        this.assenzeService.updateAssenza(assenzaID, assenzaDTO, dipendenteAutenticato);
        return new AssenzaRespDTO("assenza modificata!", assenzaID);
    }

    @GetMapping("/lista-assenze/me")
    public List<Assenza> assenzeDipendente(@AuthenticationPrincipal Dipendente dipendente) {
        return this.assenzeService.assenzeDelDipendente(dipendente);
    }

    @GetMapping("/lista-assenze/{dipendenteID}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public List<Assenza> assenzeDipendente(@PathVariable UUID dipendenteID) {
        return this.assenzeService.controlloAssenzeManager(dipendenteID);
    }

    @PatchMapping("/approvazione-assenza/{assenzaID}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AssenzaRespDTO approvazione(@PathVariable UUID assenzaID, @RequestBody @Validated AssenzaApprovazioneDTO assenzaApprovazioneDTO) {
        this.assenzeService.approvaAssenza(assenzaID, assenzaApprovazioneDTO);
        return new AssenzaRespDTO("Status assenza modificato!", assenzaID);
    }

    @GetMapping("/{assenzaID}")
    public Assenza getAssenza(@PathVariable UUID assenzaID, @AuthenticationPrincipal Dipendente dipendente) {
        return this.assenzeService.findByIDController(assenzaID, dipendente);
    }
}