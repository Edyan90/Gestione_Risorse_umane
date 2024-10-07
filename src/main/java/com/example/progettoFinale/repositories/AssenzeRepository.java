package com.example.progettoFinale.repositories;

import com.example.progettoFinale.entities.Assenza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssenzeRepository extends JpaRepository<Assenza, UUID> {
}
