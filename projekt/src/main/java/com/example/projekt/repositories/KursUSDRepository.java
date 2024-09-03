package com.example.projekt.repositories;

import com.example.projekt.models.KursCHF;
import com.example.projekt.models.KursEUR;
import com.example.projekt.models.KursUSD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KursUSDRepository extends JpaRepository<KursUSD, Integer> {
    Optional<KursUSD> findTopByOrderByIdDesc();
    List<KursUSD> findAllByOrderByIdDesc();
}
