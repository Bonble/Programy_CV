package com.example.projekt.services;

import com.example.projekt.models.*;
import com.example.projekt.repositories.KursCHFRepository;
import com.example.projekt.repositories.KursEURRepository;
import com.example.projekt.repositories.KursGBPRepository;
import com.example.projekt.repositories.KursUSDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class KursyService {
    @Autowired
    KursUSDRepository kursUSDRepository;
    @Autowired
    KursCHFRepository kursCHFRepository;
    @Autowired
    KursEURRepository kursEURRepository;
    @Autowired
    KursGBPRepository kursGBPRepository;

    public Optional<KursEUR> getEURNewest() {
        return kursEURRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getEURAll() {
        Iterable<KursEUR> k = kursEURRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursEUR> ki = k.iterator();
        while(ki.hasNext()){
            kList.add(ki.next());
        }

        return kList;
    }

    public Optional<KursUSD> getUSDNewest() {
        return kursUSDRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getUSDAll() {
        Iterable<KursUSD> k = kursUSDRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursUSD> ki = k.iterator();
        while(ki.hasNext()){
            kList.add(ki.next());
        }

        return kList;
    }

    public Optional<KursCHF> getCHFNewest() {
        return kursCHFRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getCHFAll() {
        Iterable<KursCHF> k = kursCHFRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursCHF> ki = k.iterator();
        while(ki.hasNext()){
            kList.add(ki.next());
        }

        return kList;
    }

    public Optional<KursGBP> getGBPNewest() {
        return kursGBPRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getGBPAll() {
        Iterable<KursGBP> k = kursGBPRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursGBP> ki = k.iterator();
        while(ki.hasNext()){
            kList.add(ki.next());
        }

        return kList;
    }
}
