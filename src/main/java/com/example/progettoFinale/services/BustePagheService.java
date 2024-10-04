package com.example.progettoFinale.services;

import com.example.progettoFinale.entities.BustaPaga;
import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.enums.RuoloType;
import com.example.progettoFinale.exceptions.BadRequestEx;
import com.example.progettoFinale.exceptions.NotFoundEx;
import com.example.progettoFinale.exceptions.UnauthorizedEx;
import com.example.progettoFinale.recordsDTO.BustaPagaDTO;
import com.example.progettoFinale.repositories.BustaPagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BustePagheService {
    @Autowired
    private BustaPagaRepository bustaPagaRepository;
    @Autowired
    private DipendentiService dipendentiService;

    private void checkAuthorization(Dipendente dipendente) {
        if (!dipendente.getRuolo().equals(RuoloType.ADMIN) && !dipendente.getRuolo().equals(RuoloType.MANAGER)) {
            throw new UnauthorizedEx("Non sei autorizzato a eseguire questa azione.");
        }
    }

    public Page<BustaPaga> findAll(int page, int size, String sortBy) {
        if (page > 10) page = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.bustaPagaRepository.findAll(pageable);
    }

    public BustaPaga findByID(UUID bustaID) {
        return this.bustaPagaRepository.findById(bustaID).orElseThrow(() -> new NotFoundEx(bustaID));
    }

    public BustaPaga saveBustaPaga(BustaPagaDTO bustaPagaDTO, Dipendente dipendenteAutenticato) {
        this.checkAuthorization(dipendenteAutenticato);
        Dipendente dipendente = this.dipendentiService.findByID(UUID.fromString(bustaPagaDTO.dipendenteID()));
        return this.bustaPagaRepository.save(
                new BustaPaga(dipendente, bustaPagaDTO.data(), bustaPagaDTO.importoTotale(), bustaPagaDTO.oreStraordinario()));
    }

    public void findAndDelete(UUID bustaID, Dipendente dipendenteAutenticato) {
        this.checkAuthorization(dipendenteAutenticato);
        BustaPaga bustaPaga = this.findByID(bustaID);
        this.bustaPagaRepository.delete(bustaPaga);
    }

    public BustaPaga updateBustaPaga(UUID bustaID, BustaPagaDTO bustaPagaDTO, Dipendente dipendenteAutenticato) {
        this.checkAuthorization(dipendenteAutenticato);
        BustaPaga found = this.findByID(bustaID);
        Dipendente dipendenteFromDTO = this.dipendentiService.findByID(UUID.fromString(bustaPagaDTO.dipendenteID()));
        if (!found.getDipendente().getId().equals(dipendenteFromDTO.getId())) {
            throw new BadRequestEx("Gli ID del dipendente preso dal ID della Bustapaga e quello che è stato inserito nel DTO(Body) non corrispondono");
        }
        found.setData(bustaPagaDTO.data());
        found.setImportoTotale(bustaPagaDTO.importoTotale());
        found.setOreLavorateExtra(bustaPagaDTO.oreStraordinario());
        return this.bustaPagaRepository.save(found);
    }

    public List<BustaPaga> bustepagaDipendente(UUID dipendenteID, Dipendente dipendenteAutenticato) {
        this.checkAuthorization(dipendenteAutenticato);
        Dipendente dipendente = this.dipendentiService.findByID(dipendenteID);
        return this.bustaPagaRepository.findByDipendente(dipendente);
    }

    public List<BustaPaga> findByMeseEAnno(UUID dipendenteID, int mese, int anno, Dipendente dipendenteAutenticato) {
        this.checkAuthorization(dipendenteAutenticato);
        Dipendente dipendente = this.dipendentiService.findByID(dipendenteID);
        if (!dipendente.getId().equals(dipendenteAutenticato.getId())) {
            throw new UnauthorizedEx("Non sei autorizzato a eseguire questa azione");
        }
        if (mese < 1 || mese > 12 || anno < 2020 || anno > LocalDate.now().getYear()) {
            throw new BadRequestEx("Il riferimento al mese non è adatto, scegliere tra 1 e 12 altrimenti controlla che l'anno richiesto non sia inferiore a 2020 o superiore all'anno corrente");
        }
        return this.bustaPagaRepository.findByMeseEAnno(dipendente, mese, anno);
    }

    public Double calcolaImportoTotale(UUID dipendenteID, int anno) {
        Dipendente dipendente = this.dipendentiService.findByID(dipendenteID);
        List<BustaPaga> bustePaghe = this.bustaPagaRepository.findByDipendenteAndAnno(dipendente, anno);
        return bustePaghe.stream().mapToDouble(BustaPaga::getImportoTotale).sum();
    }

    public Integer calcolaOreStraordinarioAnnuali(UUID dipendenteID, int anno) {
        Dipendente dipendente = this.dipendentiService.findByID(dipendenteID);
        List<BustaPaga> bustePaghe = this.bustaPagaRepository.findByDipendenteAndAnno(dipendente, anno);
        return bustePaghe.stream().mapToInt(BustaPaga::getOreLavorateExtra).sum();
    }
}
