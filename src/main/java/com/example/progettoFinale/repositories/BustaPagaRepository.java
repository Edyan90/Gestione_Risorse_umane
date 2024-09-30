package com.example.progettoFinale.repositories;

import com.example.progettoFinale.entities.BustaPaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface BustaPagaRepository extends JpaRepository<BustaPaga, UUID> {
}
