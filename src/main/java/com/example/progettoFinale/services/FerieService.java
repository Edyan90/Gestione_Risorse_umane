package com.example.progettoFinale.services;


import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.entities.Ferie;
import com.example.progettoFinale.enums.StatoFerie;
import com.example.progettoFinale.exceptions.NotFoundEx;
import com.example.progettoFinale.recordsDTO.FerieDTO;
import com.example.progettoFinale.repositories.FerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FerieService {
    @Autowired
    private FerieRepository ferieRepository;
    @Autowired
    private DipendentiService dipendentiService;

    public Page<Ferie> findAll(int page, int size, String sortBy) {
        if (page > 10) page = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.ferieRepository.findAll(pageable);
    }

    public Ferie findByID(UUID ferieID) {
        return this.ferieRepository.findById(ferieID).orElseThrow(() -> new NotFoundEx(ferieID));
    }

    public Ferie saveFerie(FerieDTO ferieDTO) {
        Dipendente dipendente = this.dipendentiService.findByID(UUID.fromString(ferieDTO.dipendenteID()));
        Ferie ferie = new Ferie(dipendente, ferieDTO.dataInizio(), ferieDTO.dataFine(), StatoFerie.RICHIESTO);
        this.ferieRepository.save(ferie);
        return ferie;
    }

    public void findAndDeleteFerie(UUID ferieID) {
        Ferie found = this.findByID(ferieID);
        this.ferieRepository.delete(found);
    }

    public Ferie updateFerie(UUID ferieID, FerieDTO ferieDTO) {
        Ferie found = this.findByID(ferieID);
        found.setDataInizio(ferieDTO.dataInizio());
        found.setDataInizio(ferieDTO.dataFine());
        this.ferieRepository.save(found);
        return found;
    }
    
}
