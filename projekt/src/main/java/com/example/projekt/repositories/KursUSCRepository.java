package com.example.projekt.repositories;

import com.example.projekt.models.KursTET;
import com.example.projekt.models.KursUSC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KursUSCRepository extends JpaRepository<KursUSC, Integer> {
    Optional<KursUSC> findTopByOrderByIdDesc();
    Iterable<KursUSC> findAllByOrderByIdDesc();
}