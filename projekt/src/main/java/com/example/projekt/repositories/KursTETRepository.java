package com.example.projekt.repositories;

import com.example.projekt.models.KursTEG;
import com.example.projekt.models.KursTET;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KursTETRepository extends JpaRepository<KursTET, Integer> {
    Optional<KursTET> findTopByOrderByIdDesc();
    Iterable<KursTET> findAllByOrderByIdDesc();
}