package com.example.projekt.repositories;

import com.example.projekt.models.Historia;
import com.example.projekt.models.Lokata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriaRepository extends JpaRepository<Historia, Integer> {
}
