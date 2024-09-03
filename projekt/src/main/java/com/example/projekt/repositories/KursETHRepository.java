package com.example.projekt.repositories;

import com.example.projekt.models.KursBTC;
import com.example.projekt.models.KursETH;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KursETHRepository extends JpaRepository<KursETH, Integer> {
    Optional<KursETH> findTopByOrderByIdDesc();
    Iterable<KursETH> findAllByOrderByIdDesc();
}
