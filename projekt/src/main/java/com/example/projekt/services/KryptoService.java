package com.example.projekt.services;

import com.example.projekt.models.*;
import com.example.projekt.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class KryptoService {
    @Autowired
    KursBTCRepository kursBTCRepository;
    @Autowired
    KursETHRepository kursETHRepository;
    @Autowired
    KursUSCRepository kursUSCRepository;
    @Autowired
    KursTETRepository kursTETRepository;
    @Autowired
    KursTEGRepository kursTEGRepository;
    @Autowired
    KursEUCRepository kursEUCRepository;

    public Optional<KursUSC> getUSCNewest() {
        return kursUSCRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getUSCAll() {
        Iterable<KursUSC> k = kursUSCRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursUSC> ki = k.iterator();
        KursUSC kk = new KursUSC();
        while(ki.hasNext()){
            kk = ki.next();
            kk.setKupno(kk.getKurs());
            kk.setSprzedarz(kk.getKurs());
            kList.add(kk);
        }

        return kList;
    }

    public Optional<KursBTC> getBTCNewest() {
        return kursBTCRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getBTCAll() {
        Iterable<KursBTC> k = kursBTCRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursBTC> ki = k.iterator();
        KursBTC kk = new KursBTC();
        while(ki.hasNext()){
            kk = ki.next();
            kk.setKupno(kk.getKurs());
            kk.setSprzedarz(kk.getKurs());
            kList.add(kk);
        }

        return kList;
    }

    public Optional<KursETH> getETHNewest() {
        return kursETHRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getETHAll() {
        Iterable<KursETH> k = kursETHRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursETH> ki = k.iterator();
        KursETH kk = new KursETH();
        while(ki.hasNext()){
            kk = ki.next();
            kk.setKupno(kk.getKurs());
            kk.setSprzedarz(kk.getKurs());
            kList.add(kk);
        }

        return kList;
    }

    public Optional<KursTET> getTETNewest() {
        return kursTETRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getTETAll() {
        Iterable<KursTET> k = kursTETRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursTET> ki = k.iterator();
        KursTET kk = new KursTET();
        while(ki.hasNext()){
            kk = ki.next();
            kk.setKupno(kk.getKurs());
            kk.setSprzedarz(kk.getKurs());
            kList.add(kk);
        }

        return kList;
    }

    public Optional<KursTEG> getTEGNewest() {
        return kursTEGRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getTEGAll() {
        Iterable<KursTEG> k = kursTEGRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursTEG> ki = k.iterator();
        KursTEG kk = new KursTEG();
        while(ki.hasNext()){
            kk = ki.next();
            kk.setKupno(kk.getKurs());
            kk.setSprzedarz(kk.getKurs());
            kList.add(kk);
        }

        return kList;
    }

    public Optional<KursEUC> getEUCNewest() {
        return kursEUCRepository.findTopByOrderByIdDesc();
    }

    public List<Kurs> getEUCAll() {
        Iterable<KursEUC> k = kursEUCRepository.findAllByOrderByIdDesc();
        List<Kurs> kList = new LinkedList<Kurs>();
        Iterator<KursEUC> ki = k.iterator();
        KursEUC kk= new KursEUC();
        while(ki.hasNext()){
            kk = ki.next();
            kk.setKupno(kk.getKurs());
            kk.setSprzedarz(kk.getKurs());
            kList.add(kk);
        }

        return kList;
    }
}
