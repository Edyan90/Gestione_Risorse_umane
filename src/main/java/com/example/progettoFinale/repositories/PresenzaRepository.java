package com.example.progettoFinale.repositories;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.entities.Presenza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PresenzaRepository extends JpaRepository<Presenza, UUID> {
    List<Presenza> findByDipendentId(UUID dipendenteId);

    @Query("SELECT p FROM Presenza p WHERE p.dipendente = :dipendente AND MONTH(p.data) = :mese AND YEAR(p.data) = :anno")
    List<Presenza> findByStoricoPresenzeMensile(@Param("dipendente") Dipendente dipendente,
                                                @Param("mese") int mese,
                                                @Param("anno") int anno);

}
