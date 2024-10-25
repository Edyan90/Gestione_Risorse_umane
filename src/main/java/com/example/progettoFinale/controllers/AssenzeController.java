package com.example.progettoFinale.controllers;

import com.example.progettoFinale.entities.Assenza;
import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.enums.StatoAssenza;
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

import java.time.LocalDate;
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
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findAndDelete(@PathVariable UUID assenzaID) throws Exception {
        System.out.println(assenzaID);
        this.assenzeService.findAndDeleteAssenza(assenzaID);
    }

    @DeleteMapping("/me/{assenzaID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findAndDeleteSelf(@AuthenticationPrincipal Dipendente dipendente, @PathVariable UUID assenzaID) {
        this.assenzeService.findAndDeleteAssenzaSelf(assenzaID, dipendente);
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
    public AssenzaRespDTO editAssenza(
            @RequestBody @Validated AssenzaDipendenteDTO assenzaDTO,
            @AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        this.assenzeService.updateAssenza(assenzaDTO, dipendenteAutenticato);
        return new AssenzaRespDTO("assenza modificata!", assenzaDTO.dipendenteID());
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

    @GetMapping("/stato")
    public List<Assenza> getAssenzePerStato(@RequestParam StatoAssenza stato) {
        List<Assenza> assenze = assenzeService.getAssenzePerStato(stato);
        return assenze;
    }

    @GetMapping("/periodo")//assenze/periodo?startDate=2024-01-01&endDate=2024-01-31
    public List<Assenza> getAssenzeByPeriodo(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return assenzeService.getAssenzeByPeriodo(start, end);
    }
}