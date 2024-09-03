package com.example.projekt.repositories;

import com.example.projekt.models.KursCHF;
import com.example.projekt.models.KursEUR;
import com.example.projekt.models.KursGBP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KursGBPRepository extends JpaRepository<KursGBP, Integer> {
    Optional<KursGBP> findTopByOrderByIdDesc();
    Iterable<KursGBP> findAllByOrderByIdDesc();
}
