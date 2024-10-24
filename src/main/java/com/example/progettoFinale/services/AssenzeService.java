package com.example.progettoFinale.services;

import com.example.progettoFinale.entities.Assenza;
import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.enums.RuoloType;
import com.example.progettoFinale.enums.StatoAssenza;
import com.example.progettoFinale.exceptions.BadRequestEx;
import com.example.progettoFinale.exceptions.NotFoundEx;
import com.example.progettoFinale.exceptions.UnauthorizedEx;
import com.example.progettoFinale.recordsDTO.assenzeDTO.AssenzaApprovazioneDTO;
import com.example.progettoFinale.recordsDTO.assenzeDTO.AssenzaDipendenteDTO;
import com.example.progettoFinale.recordsDTO.assenzeDTO.GiustificazioneDTO;
import com.example.progettoFinale.repositories.AssenzeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AssenzeService {
    @Autowired
    private AssenzeRepository assenzeRepository;
    @Autowired
    private DipendentiService dipendentiService;

    public Assenza findByID(UUID assenzaID) {
        return this.assenzeRepository.findById(assenzaID).orElseThrow(() -> new NotFoundEx(assenzaID));
    }

    public Assenza findByIDController(UUID assenzaID, Dipendente dipendente) {
        Assenza assenza = this.findByID(assenzaID);
        Dipendente dipendente1 = assenza.getDipendente();
        if (!dipendente1.getId().equals(dipendente.getId())
                && !dipendente.getRuolo().equals(RuoloType.MANAGER)
                && !dipendente.getRuolo().equals(RuoloType.ADMIN)) {
            throw new UnauthorizedEx("Non si autorizzato a visualizzare questo dato!");
        }
        return this.assenzeRepository.findById(assenzaID).orElseThrow(() -> new NotFoundEx(assenzaID));
    }

    public Assenza saveGiustificazione(GiustificazioneDTO giustificazioneDTO, Dipendente dipendente) {
        Assenza assenza = new Assenza(dipendente, giustificazioneDTO.data(), giustificazioneDTO.motivo());
        return this.assenzeRepository.save(assenza);
    }

    public Assenza saveAssenzaFromManager(AssenzaDipendenteDTO assenzaDTO) {
        Dipendente dipendente = this.dipendentiService.findByID(assenzaDTO.dipendenteID());
        Assenza assenza = new Assenza(dipendente, assenzaDTO.data(), assenzaDTO.motivo());
        return this.assenzeRepository.save(assenza);
    }

    @Transactional
    public void findAndDeleteAssenza(UUID assenzaID, Dipendente dipendente) throws Exception {
        Assenza assenza = this.findByID(assenzaID);
        System.out.println("Assenza trovata: " + assenza);
        if (!assenza.getDipendente().getId().equals(dipendente.getId())
                && !dipendente.getRuolo().equals(RuoloType.ADMIN)
                && !dipendente.getRuolo().equals(RuoloType.MANAGER)) {
            throw new UnauthorizedEx("Non sei autorizzato ad eliminare la presenza");
        }
        this.assenzeRepository.delete(assenza);
        this.assenzeRepository.flush();
        System.out.println("Assenza eliminata.");
    }

    public Assenza updateAssenza(AssenzaDipendenteDTO assenzaDTO, Dipendente dipendenteAutenticato) {
        Assenza found = this.findByID(assenzaDTO.dipendenteID());
        Dipendente dipendenteTrovato = found.getDipendente();
        if (!dipendenteTrovato.getId().equals(dipendenteAutenticato.getId())
                && !assenzaDTO.dipendenteID().equals(dipendenteTrovato.getId())
                && !dipendenteAutenticato.getRuolo().equals(RuoloType.ADMIN)) {
            throw new UnauthorizedEx("Non sei autorizzato ad aggiornare  questa  presenza");
        }
        found.setData(assenzaDTO.data());
        found.setMotivo(assenzaDTO.motivo());
        return this.assenzeRepository.save(found);
    }

    public List<Assenza> assenzeDelDipendente(Dipendente dipendente) {
        return this.assenzeRepository.findByDipendente(dipendente);
    }

    public List<Assenza> controlloAssenzeManager(UUID dipendenteID) {
        Dipendente dipendente = this.dipendentiService.findByID(dipendenteID);
        return this.assenzeRepository.findByDipendente(dipendente);
    }

    public void approvaAssenza(UUID assenzaID, AssenzaApprovazioneDTO assenzaApprovazioneDTO) {
        Assenza assenza = this.findByID(assenzaID);
        switch (assenzaApprovazioneDTO.statoAssenza().toLowerCase()) {
            case "approvato":
                assenza.setStato(StatoAssenza.APPROVATO);
                break;
            case "non_approvata":
                assenza.setStato(StatoAssenza.NON_APPROVATA);
                break;
            default:
                throw new BadRequestEx("Stato non valido " + assenzaApprovazioneDTO.statoAssenza() + " .I valori validi sono APPROVATO O NON_APPROVATA");
        }

        this.assenzeRepository.save(assenza);
    }

    public List<Assenza> getAssenzePerStato(StatoAssenza stato) {
        return assenzeRepository.findByStato(stato);
    }

    public List<Assenza> getAssenzeByPeriodo(LocalDate startDate, LocalDate endDate) {
        return assenzeRepository.findAssenzeBetweenDates(startDate, endDate);
    }
}