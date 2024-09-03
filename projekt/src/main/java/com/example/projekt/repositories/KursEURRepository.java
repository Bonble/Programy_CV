package com.example.projekt.repositories;

import com.example.projekt.models.KursCHF;
import com.example.projekt.models.KursEUR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KursEURRepository extends JpaRepository<KursEUR, Integer> {
    Optional<KursEUR> findTopByOrderByIdDesc();
    Iterable<KursEUR> findAllByOrderByIdDesc();
}
