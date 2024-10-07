package com.example.progettoFinale.controllers;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.recordsDTO.NewAdminDTO;
import com.example.progettoFinale.recordsDTO.dipendentiDTO.DipendenteDTO;
import com.example.progettoFinale.recordsDTO.dipendentiDTO.DipendenteRespDTO;
import com.example.progettoFinale.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {
    @Autowired
    private DipendentiService dipendentiService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Dipendente> findAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "nome") String sortBy) {
        return this.dipendentiService.findAll(page, size, sortBy);
    }

    @GetMapping("/me")
    public Dipendente getProfile(@AuthenticationPrincipal Dipendente currentAuthenticationDipendenti) {
        return currentAuthenticationDipendenti;
    }

    @DeleteMapping("/{dipendenteID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findAndDelete(@PathVariable UUID dipendenteID) {
        this.dipendentiService.findAndDelete(dipendenteID);
    }

    @PatchMapping("/me/avatar")
    public DipendenteRespDTO avatarUpload(@AuthenticationPrincipal Dipendente currentAuthenticationDipendenti, @RequestParam("avatar") MultipartFile image) {
        this.dipendentiService.avatarUpload(currentAuthenticationDipendenti.getId(), image);
        return new DipendenteRespDTO(String.valueOf(currentAuthenticationDipendenti.getId()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public DipendenteRespDTO createDipendente(@RequestBody @Validated DipendenteDTO dipendenteDTO) {
        return new DipendenteRespDTO(String.valueOf(this.dipendentiService.saveDipendente(dipendenteDTO).getId()));
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public DipendenteRespDTO updateProfile(@AuthenticationPrincipal Dipendente currentAuthenticateDipendente, @RequestBody @Validated DipendenteDTO dipendenteDTO) {
        return new DipendenteRespDTO(String.valueOf(this.dipendentiService.updateDipendente(currentAuthenticateDipendente.getId(), dipendenteDTO).getId()));
    }

    @PutMapping("/add-admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public DipendenteRespDTO addNewAdmin(@RequestBody @Validated NewAdminDTO newAdminDTO) {
        return new DipendenteRespDTO(String.valueOf(this.dipendentiService.saveNewAdmin(UUID.fromString(newAdminDTO.adminID())).getId()));
    }

    @GetMapping("/{dipendenteID}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public Dipendente getDipendente(@PathVariable UUID dipendenteID) {
        return this.dipendentiService.findByID(dipendenteID);
    }
}
