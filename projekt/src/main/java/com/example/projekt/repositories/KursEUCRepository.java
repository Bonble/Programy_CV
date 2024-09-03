package com.example.projekt.repositories;

import com.example.projekt.models.KursBTC;
import com.example.projekt.models.KursEUC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KursEUCRepository extends JpaRepository<KursEUC, Integer> {
    Optional<KursEUC> findTopByOrderByIdDesc();
    Iterable<KursEUC> findAllByOrderByIdDesc();
}

