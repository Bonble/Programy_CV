package com.example.projekt.repositories;

import com.example.projekt.models.Lokata;
import com.example.projekt.models.LokataAktywna;
import com.example.projekt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LokataAktywnaRepository extends JpaRepository<LokataAktywna, Integer> {
    Optional<LokataAktywna> findById(Long id);

}
