package com.example.progettoFinale.repositories;

import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.entities.Ferie;
import com.example.progettoFinale.enums.StatoFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FerieRepository extends JpaRepository<Ferie, UUID> {

    List<Ferie> findByDipendenteId(UUID dipendenteId);

    List<Ferie> findByStato(StatoFerie stato);

    @Query("SELECT f FROM Ferie f WHERE f.stato = :stato AND f.dataInizio >= :data1 AND f.dataFine <= :data2")
    List<Ferie> findByDateRangeAndStato(LocalDate data1, LocalDate data2, StatoFerie stato);

    @Query("SELECT f FROM Ferie f WHERE f.dipendente=:dipendente AND f.stato <> 'RICHIESTO'")
    List<Ferie> findByStoricoFerie(@Param("dipendente") Dipendente dipendente);

    @Query("SELECT f FROM Ferie f WHERE f.dipendente = :dipendente AND " +
            "(f.dataInizio <= :dataFine AND f.dataFine >= :dataInizio)")
    List<Ferie> findFerieSovrapposte(@Param("dipendente") Dipendente dipendente,
                                     @Param("dataInizio") LocalDate dataInizio,
                                     @Param("dataFine") LocalDate dataFine);


}
