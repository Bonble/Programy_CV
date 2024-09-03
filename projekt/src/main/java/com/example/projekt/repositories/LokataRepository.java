package com.example.projekt.repositories;

import com.example.projekt.models.Lokata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LokataRepository extends JpaRepository<Lokata, Integer> {
    Optional<Lokata> findById(Long id);

    //Optional<ProduktItem> findByName(Long ean);
}
