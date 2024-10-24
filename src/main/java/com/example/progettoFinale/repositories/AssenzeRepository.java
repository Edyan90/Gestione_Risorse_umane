package com.example.progettoFinale.repositories;

import com.example.progettoFinale.entities.Assenza;
import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.enums.StatoAssenza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssenzeRepository extends JpaRepository<Assenza, UUID> {
    List<Assenza> findByDipendente(Dipendente dipendente);

    List<Assenza> findByStato(StatoAssenza stato);

    @Query("SELECT a FROM Assenza a WHERE a.data BETWEEN :startDate AND :endDate")
    List<Assenza> findAssenzeBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
