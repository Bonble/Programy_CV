package com.example.projekt.services;

import com.example.projekt.models.Lokata;
import com.example.projekt.models.User;
import com.example.projekt.models.WalutaKupiona;
import com.example.projekt.repositories.WalutaKupionaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalutaKupionaService {

    @Autowired
    private WalutaKupionaRepository walutaKupionaRepository;


    public List<WalutaKupiona> getAll() {
        return walutaKupionaRepository.findAll();
    }

    public WalutaKupiona save(WalutaKupiona walutaKupiona){
        WalutaKupiona walutaKupionaSave = walutaKupiona;
        List<WalutaKupiona> walutaKupionaList = walutaKupionaRepository.findAll();
        for(WalutaKupiona w:walutaKupionaList) {
            if(w.getUser_id().getId().equals(walutaKupiona.getUser_id().getId()) && w.getNazwa().equals(walutaKupiona.getNazwa())){
                System.out.println("jest");
                walutaKupionaSave=w;
                walutaKupionaSave.setIlosc(walutaKupionaSave.getIlosc().add(walutaKupiona.getIlosc()));
            }
        }

        walutaKupionaRepository.save(walutaKupionaSave);
        return walutaKupionaSave;
    }
}
