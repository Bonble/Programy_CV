package com.example.projekt.services;

import com.example.projekt.models.Lokata;
import com.example.projekt.repositories.LokataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LokataService {

    @Autowired
    private LokataRepository lokataRepository;

    public Optional<Lokata> getById(Long id) {
        return lokataRepository.findById(id);
    }

    public Iterable<Lokata> getAll() {
        return lokataRepository.findAll();
    }

    public void delete(Lokata lokata) {
        lokataRepository.delete(lokata);
    }
}
