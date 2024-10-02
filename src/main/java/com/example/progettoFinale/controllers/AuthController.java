package com.example.progettoFinale.controllers;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.exceptions.BadRequestEx;
import com.example.progettoFinale.recordsDTO.DipendenteDTO;
import com.example.progettoFinale.recordsDTO.DipendenteRespDTO;
import com.example.progettoFinale.recordsDTO.LoginDTO;
import com.example.progettoFinale.recordsDTO.TokenDTO;
import com.example.progettoFinale.services.AuthService;
import com.example.progettoFinale.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private DipendentiService dipendentiService;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody @Validated LoginDTO loginDTO) {
        return new TokenDTO(this.authService.checkCredenzialiAndGeneraToken(loginDTO));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public DipendenteRespDTO createDipendente(@RequestBody @Validated DipendenteDTO dipendenteDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new BadRequestEx("Ci sono stati errori nel payload " + messages);
        } else {
            Dipendente dipendente = this.dipendentiService.saveDipendente(dipendenteDTO);
            return new DipendenteRespDTO(String.valueOf(dipendente.getId()));
        }
    }
}
