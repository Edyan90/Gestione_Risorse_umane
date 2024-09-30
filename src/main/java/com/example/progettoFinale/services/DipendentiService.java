package com.example.progettoFinale.services;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.exceptions.NotFoundEx;
import com.example.progettoFinale.repositories.DipendentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DipendentiService {
    @Autowired
    private  DipendentiRepository dipendentiRepository;

    public Page<Dipendente> findAll(int page, int size, String sortBy){
        if (page>10) page =10;
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortBy));
        return this.dipendentiRepository.findAll(pageable);
    }
     public Dipendente findByID(UUID id){
        return this.dipendentiRepository.findById(id).orElseThrow(()->new NotFoundEx(id));
     }
     
}
