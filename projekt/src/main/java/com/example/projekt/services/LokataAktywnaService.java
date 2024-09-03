package com.example.projekt.services;

import com.example.projekt.models.LokataAktywna;
import com.example.projekt.repositories.LokataAktywnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LokataAktywnaService {

    @Autowired
    private LokataAktywnaRepository lokataAktywnaRepository;

    public Optional<LokataAktywna> getById(Long id) {
        return lokataAktywnaRepository.findById(id);
    }

    public Iterable<LokataAktywna> getAll() {
        return lokataAktywnaRepository.findAll();
    }

    public LokataAktywna save(LokataAktywna lokataAktywna) {
        return lokataAktywnaRepository.save(lokataAktywna);
    }

    public void delete(LokataAktywna lokataAktywna) {
        lokataAktywnaRepository.delete(lokataAktywna);
    }
}
