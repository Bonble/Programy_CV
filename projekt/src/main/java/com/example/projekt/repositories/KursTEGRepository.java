package com.example.projekt.repositories;

import com.example.projekt.models.KursEUC;
import com.example.projekt.models.KursTEG;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KursTEGRepository extends JpaRepository<KursTEG, Integer> {
    Optional<KursTEG> findTopByOrderByIdDesc();
    Iterable<KursTEG> findAllByOrderByIdDesc();
}