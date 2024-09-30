package com.example.progettoFinale.services;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.exceptions.UnauthorizedEx;
import com.example.progettoFinale.recordsDTO.LoginDTO;
import com.example.progettoFinale.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private DipendentiService dipendentiService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredenzialiAndGeneraToken(LoginDTO loginDTO) {
        Dipendente dipendente = dipendentiService.findByUsername(loginDTO.username());
        if (bcrypt.matches(loginDTO.password(), dipendente.getPassword())) {
            return jwtTools.createToken(dipendente);
        } else {
            throw new UnauthorizedEx("Credenziali errate!");
        }
    }
}
