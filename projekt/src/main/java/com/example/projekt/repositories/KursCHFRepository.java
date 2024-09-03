package com.example.projekt.repositories;

import com.example.projekt.models.Historia;
import com.example.projekt.models.KursCHF;
import com.example.projekt.models.KursEUR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KursCHFRepository extends JpaRepository<KursCHF, Integer> {
    Optional<KursCHF> findTopByOrderByIdDesc();
    Iterable<KursCHF> findAllByOrderByIdDesc();
}
