package com.example.projekt.services;

import com.example.projekt.models.Historia;
import com.example.projekt.models.LokataAktywna;
import com.example.projekt.repositories.HistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoriaService {
    @Autowired
    private HistoriaRepository historiaRepository;

    public Iterable<Historia> getAll() {
        return historiaRepository.findAll();
    }

    public Historia save(Historia historia) {
        return historiaRepository.save(historia);
    }
}
