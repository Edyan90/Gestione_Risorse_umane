package com.example.progettoFinale.services;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.entities.Presenza;
import com.example.progettoFinale.enums.RuoloType;
import com.example.progettoFinale.enums.StatoPresenza;
import com.example.progettoFinale.exceptions.BadRequestEx;
import com.example.progettoFinale.exceptions.NotFoundEx;
import com.example.progettoFinale.exceptions.UnauthorizedEx;
import com.example.progettoFinale.recordsDTO.PresenzaApprovazionMensileDTO;
import com.example.progettoFinale.recordsDTO.PresenzaApprovazioneDTO;
import com.example.progettoFinale.recordsDTO.PresenzaDTO;
import com.example.progettoFinale.repositories.PresenzaRepository;
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
public class PresenzeService {
    @Autowired
    private PresenzaRepository presenzaRepository;
    @Autowired
    private DipendentiService dipendentiService;

    public Page<Presenza> findAll(int page, int size, String sortBy) {
        if (page > 10) page = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.presenzaRepository.findAll(pageable);
    }

    public Presenza findByID(UUID presenzaID) {
        return this.presenzaRepository.findById(presenzaID).orElseThrow(() -> new NotFoundEx(presenzaID));
    }

    public Presenza savePresenza(PresenzaDTO presenzaDTO, Dipendente dipendente) {
        if (!presenzaDTO.dipendenteID().equals(dipendente.getId())
                && !dipendente.getRuolo().equals(RuoloType.ADMIN)
                && !dipendente.getRuolo().equals(RuoloType.MANAGER)) {
            throw new UnauthorizedEx("Non sei autorizzato a salvare la presenza per un altro dipendente");
        }
        return this.presenzaRepository.save(new Presenza(dipendente, presenzaDTO.data(), presenzaDTO.presente(), StatoPresenza.IN_ATTESA));
    }

    public void findAndDeletePresenza(UUID presenzaID, Dipendente dipendenteAutenticato) {
        Presenza found = this.findByID(presenzaID);
        Dipendente dipendente = found.getDipendente();
        if (!dipendente.getId().equals(dipendenteAutenticato.getId())
                && !dipendenteAutenticato.getRuolo().equals(RuoloType.ADMIN)
                && !dipendenteAutenticato.getRuolo().equals(RuoloType.MANAGER)) {
            throw new UnauthorizedEx("Non sei autorizzato ad eliminare la presenza");
        }
        this.presenzaRepository.delete(found);
    }

    public Presenza updatePresenza(UUID presenzaID, PresenzaDTO presenzaDTO, Dipendente dipendenteAutenticato) {
        Presenza found = this.findByID(presenzaID);
        Dipendente dipendenteTrovato = found.getDipendente();

        if (!dipendenteTrovato.getId().equals(dipendenteAutenticato.getId())
                && !presenzaDTO.dipendenteID().equals(dipendenteTrovato.getId())
                && !dipendenteAutenticato.getRuolo().equals(RuoloType.ADMIN)) {
            throw new UnauthorizedEx("Non sei autorizzato ad aggiornare  questa  presenza");
        }

        found.setData(presenzaDTO.data());
        found.setPresente(presenzaDTO.presente());

        return this.presenzaRepository.save(found);

    }

    public List<Presenza> presenzeDelDipendente(UUID dipendenteID) {
        Dipendente dipendente = this.dipendentiService.findByID(dipendenteID);
        return this.presenzaRepository.findByDipendente(dipendente);
    }

    public List<Presenza> presenzaMensile(Dipendente dipendente, int mese, int anno) {
        if (mese < 1 || mese > 12 || anno < 2020 || anno > LocalDate.now().getYear()) {
            throw new BadRequestEx("Il riferimento al mese non è adatto, scegliere tra 1 e 12 altrimenti controlla che l'anno richiesto non sia inferire a 2020 o superiore all'anno corrente");
        }
        return this.presenzaRepository.findByStoricoPresenzeMensile(dipendente, mese, anno);
    }

    //QUESTO è PER IL MANAGER
    public List<Presenza> presenzaMensile(UUID dipendenteID, int mese, int anno) {
        Dipendente dipendente = this.dipendentiService.findByID(dipendenteID);
        if (mese < 1 || mese > 12 || anno < 2020 || anno > LocalDate.now().getYear()) {
            throw new BadRequestEx("Il riferimento al mese non è adatto, scegliere tra 1 e 12 altrimenti controlla che l'anno richiesto non sia inferire a 2020 o superiore all'anno corrente");
        }
        return this.presenzaRepository.findByStoricoPresenzeMensile(dipendente, mese, anno);
    }

    public void approvaPresenza(UUID presenzaID, PresenzaApprovazioneDTO presenzaApprovazioneDTO) {
        Presenza presenza = this.findByID(presenzaID);
        switch (presenzaApprovazioneDTO.statoPresenza().toLowerCase()) {
            case "approvato":
                presenza.setStatoPresenza(StatoPresenza.APPROVATO);
                break;
            case "non_approvata":
                presenza.setStatoPresenza(StatoPresenza.NON_APPROVATA);
                break;
            default:
                throw new BadRequestEx("Stato non valido " + presenzaApprovazioneDTO.statoPresenza() + " .I valori validi sono APPROVATO O NON_APPROVATA");
        }

        this.presenzaRepository.save(presenza);
    }

    public void approvaPresenzeMensili(UUID dipendenteID, PresenzaApprovazionMensileDTO presenzaApprovazionMensileDTO) {
        Dipendente dipendente = this.dipendentiService.findByID(dipendenteID);
        List<Presenza> presenze = presenzaMensile(dipendente, presenzaApprovazionMensileDTO.mese(), presenzaApprovazionMensileDTO.anno());
        switch (presenzaApprovazionMensileDTO.statoPresenza().toLowerCase()) {
            case "approvato":
                presenze.stream().filter(presenza -> presenza.getStatoPresenza().equals(StatoPresenza.IN_ATTESA))
                        .forEach(presenza -> {
                            presenza.setStatoPresenza(StatoPresenza.APPROVATO);
                        });
                break;
            case "non_approvato":
                presenze.stream().filter(presenza -> presenza.getStatoPresenza().equals(StatoPresenza.IN_ATTESA))
                        .forEach(presenza -> {
                            presenza.setStatoPresenza(StatoPresenza.NON_APPROVATA);
                        });
                break;
            default:
                throw new BadRequestEx("Stato non valido " + presenzaApprovazionMensileDTO.statoPresenza() + " .I valori validi sono APPROVATO O NON_APPROVATA");
        }
        this.presenzaRepository.saveAll(presenze);

    }

}
