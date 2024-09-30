package com.example.progettoFinale.services;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.enums.RuoloType;
import com.example.progettoFinale.exceptions.BadRequestEx;
import com.example.progettoFinale.exceptions.NotFoundEx;
import com.example.progettoFinale.recordsDTO.DipendenteDTO;
import com.example.progettoFinale.repositories.DipendentiRepository;
import com.example.progettoFinale.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DipendentiService {
    @Autowired
    private  DipendentiRepository dipendentiRepository;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private MailgunSender mailgunSender;

    public Page<Dipendente> findAll(int page, int size, String sortBy){
        if (page>10) page =10;
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortBy));
        return this.dipendentiRepository.findAll(pageable);
    }
     public Dipendente findByID(UUID id){
        return this.dipendentiRepository.findById(id).orElseThrow(()->new NotFoundEx(id));
     }

     public Dipendente saveDipendente (DipendenteDTO dipendenteDTO){
        this.dipendentiRepository.findByEmail(dipendenteDTO.email()).ifPresent(dipendente -> {
            throw new BadRequestEx("l'email "+dipendenteDTO.email()+" è già in uso!");
        });
        this.dipendentiRepository.findByUsername(dipendenteDTO.username()).ifPresent(dipendente ->{
            throw new BadRequestEx("l'username "+dipendenteDTO.username()+" è già stato utilizzato!");
        } );
        Dipendente dipendente= new Dipendente(
                dipendenteDTO.nome(),
                dipendenteDTO.cognome(),
                dipendenteDTO.email(),
                dipendenteDTO.username(),
                Double.parseDouble(dipendenteDTO.stipendio()),
                bcrypt.encode(dipendenteDTO.password())
        );
         switch (dipendenteDTO.ruolo().toLowerCase()) {
             case "dipendente":
                 dipendente.setRuolo(RuoloType.DIPENDENTE);
                 break;
             case "manager":
                dipendente.setRuolo(RuoloType.MANAGER);
                 break;

             default:
                 throw new BadRequestEx("Stato non valido: " + dipendenteDTO.ruolo() +
                         ". I valori validi sono: DIPENDENTE E MANAGER.");
         }
         this.dipendentiRepository.save(dipendente);
         return dipendente;
     }
     public Dipendente saveNewAdmin(UUID dipendenteID){
        Dipendente dipendente= this.findByID(dipendenteID);
        dipendente.setRuolo(RuoloType.ADMIN);
         mailgunSender.sendRegistrationEmail(dipendente);
        return dipendente;
     }

}
