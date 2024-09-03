package com.example.projekt.repositories;

import com.example.projekt.models.KursBTC;
import com.example.projekt.models.KursEUR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KursBTCRepository extends JpaRepository<KursBTC, Integer> {
    Optional<KursBTC> findTopByOrderByIdDesc();
    Iterable<KursBTC> findAllByOrderByIdDesc();
}
