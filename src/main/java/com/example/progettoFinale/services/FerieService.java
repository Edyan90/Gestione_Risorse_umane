package com.example.progettoFinale.services;


import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.entities.Ferie;
import com.example.progettoFinale.enums.RuoloType;
import com.example.progettoFinale.enums.StatoFerie;
import com.example.progettoFinale.exceptions.BadRequestEx;
import com.example.progettoFinale.exceptions.NotFoundEx;
import com.example.progettoFinale.exceptions.UnauthorizedEx;
import com.example.progettoFinale.recordsDTO.FerieApprovazioneDTO;
import com.example.progettoFinale.recordsDTO.FerieDTO;
import com.example.progettoFinale.recordsDTO.FerieListDateStatoDTO;
import com.example.progettoFinale.recordsDTO.FerieRespDTO;
import com.example.progettoFinale.repositories.FerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    public Ferie saveFerie(FerieDTO ferieDTO, Dipendente currentDipendente) {
//        Dipendente found = this.dipendentiService.findByID(UUID.fromString(ferieDTO.dipendenteID()));
//        if (!currentDipendente.getId().equals(found) &&
//                !currentDipendente.getRuolo().equals(RuoloType.ADMIN)) {
//            throw new UnauthorizedEx("Non sei autorizzato a creare ferie per un altro dipendente");
//        }
        if (ferieDTO.dataInizio().isBefore(LocalDate.now()) || ferieDTO.dataInizio().equals(LocalDate.now())) {
            throw new BadRequestEx("la data d'inizio non può essere prima o uguale alla data odierna");
        }
        if (ferieDTO.dataFine().isBefore(ferieDTO.dataInizio()) || ferieDTO.dataFine().isEqual(ferieDTO.dataInizio())) {
            throw new BadRequestEx("La data di fine non può essere prima o uguale alla data d'inizio.");
        }
        List<Ferie> ferieSovrapposte = ferieRepository.findFerieSovrapposte(currentDipendente, ferieDTO.dataInizio(), ferieDTO.dataFine());
        if (!ferieSovrapposte.isEmpty()) {
            throw new BadRequestEx("Esiste già una richiesta di ferie sovrapposta per questo dipendente.");
        }
        Ferie ferie = new Ferie(currentDipendente,
                ferieDTO.dataInizio(),
                ferieDTO.dataFine(),
                StatoFerie.RICHIESTO,
                calcolaFerieMaturate(currentDipendente.getDataAssunzione()
                ));
        this.ferieRepository.save(ferie);
        return ferie;
    }

    public void findAndDeleteFerie(UUID ferieID, Dipendente currentDipendente) {
        Ferie found = this.findByID(ferieID);

        if (!found.getDipendente().getId().equals(currentDipendente.getId()) &&
                !currentDipendente.getRuolo().equals(RuoloType.ADMIN)) {
            throw new UnauthorizedEx("Non sei autorizzato a cancellare questa richiesta di ferie.");
        }

        this.ferieRepository.delete(found);
    }

    public Ferie updateFerie(UUID ferieID, FerieDTO ferieDTO, Dipendente dipendente) {
        Ferie found = this.findByID(ferieID);
        if (!found.getDipendente().getId().equals(dipendente.getId()) &&
                !dipendente.getRuolo().equals(RuoloType.ADMIN)) {
            throw new UnauthorizedEx("Non sei autorizzato a cancellare questa richiesta di ferie.");
        }
        if (ferieDTO.dataInizio().isBefore(LocalDate.now()) || ferieDTO.dataInizio().equals(LocalDate.now())) {
            throw new BadRequestEx("la data d'inizio non può essere prima o uguale alla data odierna");
        }
        if (ferieDTO.dataFine().isBefore(ferieDTO.dataInizio()) || ferieDTO.dataFine().isEqual(ferieDTO.dataInizio())) {
            throw new BadRequestEx("La data di fine non può essere prima o uguale alla data d'inizio.");
        }
        List<Ferie> ferieSovrapposte = ferieRepository.findFerieSovrapposte(dipendente, ferieDTO.dataInizio(), ferieDTO.dataFine());
        if (!ferieSovrapposte.isEmpty()) {
            throw new BadRequestEx("Esiste già una richiesta di ferie sovrapposta per questo dipendente.");
        }
        found.setDataInizio(ferieDTO.dataInizio());
        found.setDataFine(ferieDTO.dataFine());
        this.ferieRepository.save(found);
        return found;
    }

    public List<Ferie> ferieDelDipendente(UUID dipendenteID) {
        Dipendente dipendente = this.dipendentiService.findByID(dipendenteID);
        return this.ferieRepository.findByDipendenteId(dipendente.getId());
    }

    public FerieRespDTO approvazioneFerie(UUID ferieID, FerieApprovazioneDTO ferieApprovazioneDTO) {
        Ferie found = this.findByID(ferieID);
        switch (ferieApprovazioneDTO.approvazione().toLowerCase()) {
            case "approvato":
                found.setStato(StatoFerie.APPROVATO);
                break;
            case "rifiutato":
                found.setStato(StatoFerie.RIFIUTATO);
                break;
            default:
                throw new BadRequestEx("Stato non valido: " + ferieApprovazioneDTO.approvazione() + ". I valori validi sono APPROVATO O RIFIUTATO.");
        }
        return new FerieRespDTO(String.valueOf(this.ferieRepository.save(found)));
    }

    public List<Ferie> ferieStatoList(String stato) {
        switch (stato.toLowerCase()) {
            case "richiesto":
                return this.ferieRepository.findByStato(StatoFerie.RICHIESTO);

            case "approvato":
                return this.ferieRepository.findByStato(StatoFerie.APPROVATO);

            case "rifiutato":
                return this.ferieRepository.findByStato(StatoFerie.RIFIUTATO);

            default:
                throw new BadRequestEx("Stato non valido " + stato + " .I valori validi sono RICHIESTO, APPROVATO e RIFIUTATO");
        }
    }

    public List<Ferie> ferieStatoTraDate(FerieListDateStatoDTO ferieListDateStatoDTO) {
        StatoFerie statoFerie;
        try {
            statoFerie = StatoFerie.valueOf(ferieListDateStatoDTO.stato().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestEx("Stato non valido " + ferieListDateStatoDTO.stato() + ". I valori validi sono RICHIESTO, APPROVATO e RIFIUTATO");
        }

        return this.ferieRepository.findByDateRangeAndStato(ferieListDateStatoDTO.dataInizio(), ferieListDateStatoDTO.dataFine(), statoFerie);
    }

    public List<Ferie> getStorico(Dipendente dipendente) {
        return this.ferieRepository.findByStoricoFerie(dipendente);
    }


    public int calcolaFerieMaturate(LocalDate dataAssunzione) {
        LocalDate oggi = LocalDate.now();
        long giorniTrascorsi = ChronoUnit.DAYS.between(dataAssunzione, oggi);
        int giorniNellAnno = oggi.isLeapYear() ? 366 : 365;
        int ferieMaturate = (int) ((giorniTrascorsi * 26) / giorniNellAnno);
        return ferieMaturate;
    }

}
