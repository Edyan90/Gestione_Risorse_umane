package com.example.progettoFinale.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.enums.RuoloType;
import com.example.progettoFinale.exceptions.BadRequestEx;
import com.example.progettoFinale.exceptions.NotFoundEx;
import com.example.progettoFinale.recordsDTO.dipendentiDTO.DipendenteDTO;
import com.example.progettoFinale.repositories.DipendentiRepository;
import com.example.progettoFinale.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class DipendentiService {
    @Autowired
    private DipendentiRepository dipendentiRepository;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private MailgunSender mailgunSender;
    @Autowired
    private Cloudinary cloudinary;

    public Page<Dipendente> findAll(int page, int size, String sortBy) {
        if (page > 10) page = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendentiRepository.findAll(pageable);
    }


    public Dipendente findByID(UUID id) {
        return this.dipendentiRepository.findById(id).orElseThrow(() -> new NotFoundEx(id));
    }

    public Dipendente saveDipendente(DipendenteDTO dipendenteDTO) {
        this.dipendentiRepository.findByEmail(dipendenteDTO.email()).ifPresent(dipendente -> {
            throw new BadRequestEx("l'email " + dipendenteDTO.email() + " è già in uso!");
        });
        this.dipendentiRepository.findByUsername(dipendenteDTO.username()).ifPresent(dipendente -> {
            throw new BadRequestEx("l'username " + dipendenteDTO.username() + " è già stato utilizzato!");
        });
        Dipendente dipendente = new Dipendente(
                dipendenteDTO.nome(),
                dipendenteDTO.cognome(),
                dipendenteDTO.email(),
                dipendenteDTO.username(),
                Double.parseDouble(dipendenteDTO.stipendio()),
                bcrypt.encode(dipendenteDTO.password()),
                dipendenteDTO.dataAssunzione()
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
        dipendente.setAvatar("https://ui-avatars.com/api/?name=" + dipendenteDTO.nome() + "+" + dipendenteDTO.cognome());
        this.dipendentiRepository.save(dipendente);
        return dipendente;
    }


    public Dipendente saveNewAdmin(UUID dipendenteID) {
        Dipendente dipendente = this.findByID(dipendenteID);
        dipendente.setRuolo(RuoloType.ADMIN);
        mailgunSender.sendRegistrationEmail(dipendente);
        this.dipendentiRepository.save(dipendente);
        return dipendente;
    }

    public void findAndDelete(UUID utenteID) {
        Dipendente dipendente = this.findByID(utenteID);
        this.dipendentiRepository.delete(dipendente);
    }

    public Dipendente updateDipendente(UUID dipendenteID, DipendenteDTO dipendenteDTO) {
        Dipendente found = this.findByID(dipendenteID);
        this.dipendentiRepository.findByEmail(dipendenteDTO.email()).ifPresent(dipendente -> {
            if (!dipendente.getEmail().equals(dipendenteDTO.email())) {
                throw new BadRequestEx("L'email " + dipendenteDTO.email() + " è già in uso!");
            }
        });
        found.setNome(dipendenteDTO.nome());
        found.setCognome(dipendenteDTO.cognome());
        found.setEmail(dipendenteDTO.email());
        found.setStipendio(Double.parseDouble(dipendenteDTO.stipendio()));
        found.setUsername(dipendenteDTO.username());
        found.setNome(dipendenteDTO.password());
        found.setDataAssunzione(dipendenteDTO.dataAssunzione());
        switch (dipendenteDTO.ruolo().toLowerCase()) {
            case "dipendente":
                found.setRuolo(RuoloType.DIPENDENTE);
                break;
            case "manager":
                found.setRuolo(RuoloType.MANAGER);
                break;

            default:
                throw new BadRequestEx("Stato non valido: " + dipendenteDTO.ruolo() +
                        ". I valori validi sono: DIPENDENTE E MANAGER.");
        }
        return this.dipendentiRepository.save(found);
    }


    public Dipendente avatarUpload(UUID dipendenteID, MultipartFile file) {
        try {
            String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
            Dipendente dipendente = this.findByID(dipendenteID);
            dipendente.setAvatar(url);
            return this.dipendentiRepository.save(dipendente);
        } catch (IOException e) {
            throw new BadRequestEx("Errore nel caricamento del file. Verifica il formato e le dimensioni!");
        }
    }

    public Dipendente findByUsername(String username) {
        return this.dipendentiRepository.findByUsername(username).orElseThrow(() -> new NotFoundEx(username));
    }
}
