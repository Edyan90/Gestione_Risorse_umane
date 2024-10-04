package com.example.progettoFinale.repositories;

import com.example.progettoFinale.entities.BustaPaga;
import com.example.progettoFinale.entities.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BustaPagaRepository extends JpaRepository<BustaPaga, UUID> {

    List<BustaPaga> findByDipendente(UUID dipendente);

    @Query("SELECT b FROM BustaPaga b WHERE b.dipendente = :dipendente AND MONTH(b.data) = :mese AND YEAR(b.data) = :anno")
    List<BustaPaga> findByMeseEAnno(@Param("dipendente") Dipendente dipendente,
                                    @Param("mese") int mese,
                                    @Param("anno") int anno);

    List<BustaPaga> findByDipendenteAndAnno(@Param("dipendente") Dipendente dipendente,
                                            @Param("anno") int anno);
}
