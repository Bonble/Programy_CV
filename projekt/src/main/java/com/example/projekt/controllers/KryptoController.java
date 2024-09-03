package com.example.projekt.controllers;

import com.example.projekt.models.*;
import com.example.projekt.scheduled.StatusStrony;
import com.example.projekt.services.HistoriaService;
import com.example.projekt.services.KryptoService;
import com.example.projekt.services.UserService;
import com.example.projekt.services.WalutaKupionaService;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class KryptoController {
    @Autowired
    private UserService userService;

    @Autowired
    private WalutaKupionaService walutaKupionaService;

    @Autowired
    private HistoriaService historiaService;

    @Autowired
    private KryptoService kryptoService;

    @GetMapping("/krypto")
    public ModelAndView krypto(LokataAktywnaDto lokataAktywna) {
        StatusStrony statusStrony = StatusStrony.getInstance();
        if(statusStrony.getMaintenece() || statusStrony.getMainteneceAdmin()){
            return new ModelAndView("maintenance");
        }
        ModelAndView modelAndView = new ModelAndView("krypto");

        if(kryptoService.getEUCNewest().isPresent() &&
        kryptoService.getUSCNewest().isPresent() &&
        kryptoService.getBTCNewest().isPresent() &&
        kryptoService.getETHNewest().isPresent() &&
        kryptoService.getTEGNewest().isPresent() &&
        kryptoService.getTETNewest().isPresent()) {

            int id = 0;


            KursDTO kursBTC = new KursDTO("Bitcoin");
            kursBTC.setDane(kryptoService.getBTCAll());
            kursBTC.setId((long) id++);


            KursDTO kursEHT = new KursDTO("Ethereum");
            kursEHT.setDane(kryptoService.getETHAll());
            kursEHT.setId((long) id++);

            KursDTO kursTET = new KursDTO("Tether");
            kursTET.setDane(kryptoService.getTETAll());
            kursTET.setId((long) id++);

            KursDTO kursTEG = new KursDTO("Tether-gold");
            kursTEG.setDane(kryptoService.getTEGAll());
            kursTEG.setId((long) id++);

            KursDTO kursUSC = new KursDTO("Usd-coin");
            kursUSC.setDane(kryptoService.getUSCAll());
            kursUSC.setId((long) id++);

            KursDTO kursEUC = new KursDTO("Euro-coin");
            kursEUC.setDane(kryptoService.getEUCAll());
            kursEUC.setId((long) id++);


            List<KursDTO> kursy = new LinkedList<>();
            kursy.add(kursBTC);
            kursy.add(kursEHT);
            kursy.add(kursEUC);
            kursy.add(kursTET);
            kursy.add(kursUSC);
            kursy.add(kursTEG);


            List<WalutaKupiona> walutaKupionaList = walutaKupionaService.getAll();
            Collection auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            ;
            String rola = auth.toString();
            if (rola.equals("[ROLE_USER]")) {
                modelAndView = new ModelAndView("krypto_user");
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String zalogowanyLogin = authentication.getName();
                User user = userService.getUserByUsername(zalogowanyLogin);
                for (WalutaKupiona w : walutaKupionaList) {
                    if (w.getUser_id().getId().equals(user.getId())) {
                        for (KursDTO k : kursy) {
                            if (k.getNazwa().equals(w.getNazwa())) {
                                k.setIlosc(w.getIlosc());
                            }
                        }
                    }
                }
                String name = SecurityContextHolder.getContext().getAuthentication().getName();
                modelAndView.addObject("name", name);
            }

            modelAndView.addObject("walutaItems", kursy);

            List<Kurs> kursyRok = kryptoService.getBTCAll();
            List<BigDecimal> srednieBTC = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednieBTC.add(z.getKupno());
            }
            Gson gson = new Gson();
            Collections.reverse(srednieBTC);
            String kursyJson = gson.toJson(srednieBTC);
            modelAndView.addObject("btc", kursyJson);

            kursyRok = kryptoService.getETHAll();
            List<BigDecimal> srednieETH = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednieETH.add(z.getKupno());
            }
            gson = new Gson();
            Collections.reverse(srednieETH);
            kursyJson = gson.toJson(srednieETH);
            modelAndView.addObject("eth", kursyJson);

            kursyRok = kryptoService.getTEGAll();
            List<BigDecimal> srednieTEG = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednieTEG.add(z.getKupno());
            }
            gson = new Gson();
            Collections.reverse(srednieTEG);
            kursyJson = gson.toJson(srednieTEG);
            modelAndView.addObject("teg", kursyJson);

            kursyRok = kryptoService.getEUCAll();
            List<BigDecimal> srednieEUC = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednieEUC.add(z.getKupno());
            }
            gson = new Gson();
            Collections.reverse(srednieEUC);
            kursyJson = gson.toJson(srednieEUC);
            modelAndView.addObject("euc", kursyJson);

            kursyRok = kryptoService.getTETAll();
            List<BigDecimal> srednieTET = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednieTET.add(z.getKupno());
            }
            gson = new Gson();
            Collections.reverse(srednieTET);
            kursyJson = gson.toJson(srednieTET);
            modelAndView.addObject("tet", kursyJson);

            kursyRok = kryptoService.getUSCAll();
            List<BigDecimal> srednieUSC = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednieUSC.add(z.getKupno());
            }
            gson = new Gson();
            Collections.reverse(srednieUSC);
            kursyJson = gson.toJson(srednieUSC);
            modelAndView.addObject("usc", kursyJson);
        }
        return modelAndView;
    }

    @PostMapping("/operacjaKrypto")
    public String operacjaKrypto(@Valid KursDTO kursDTO, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String zalogowanyLogin = authentication.getName();
        User user = userService.getUserByUsername(zalogowanyLogin);

        WalutaKupiona walutaKupiona = new WalutaKupiona();


        walutaKupiona.setUser_id(user);
        walutaKupiona.setNazwa(kursDTO.getNazwa());

        if(kursDTO.getSprzedarz()==null){
            List<WalutaKupiona> walutaKupionaList = walutaKupionaService.getAll();
            walutaKupiona.setIlosc(kursDTO.getIlosc().multiply(new BigDecimal(-1)));

            boolean jest=false;
            for(WalutaKupiona w:walutaKupionaList) {
                if(kursDTO.getNazwa().equals(w.getNazwa()) && w.getUser_id().getId().equals(user.getId())) {
                    if (kursDTO.getIlosc().doubleValue() > w.getIlosc().doubleValue()) {
                        return "redirect:/krypto?zaDuzo";
                    }
                    else{   jest=true;
                        break;}
                }
            }
            if(!jest){
                return "redirect:/krypto?zaDuzo";
            }
            walutaKupiona.setCzy_krypto(true);
            walutaKupionaService.save(walutaKupiona);

            Historia historia = new Historia();
            historia.setTyp("Kryptowaluta");

            Calendar cal = Calendar.getInstance();
            Timestamp ts2 = new Timestamp(System.currentTimeMillis());
            cal.setTime(ts2);
            historia.setData_koniec(null);
            historia.setData_start(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())));

            historia.setWklad(null);
            historia.setZwrot(kursDTO.getKupno().multiply(kursDTO.getIlosc()));
            historia.setUser_id(user);
            historia.setNazwa(kursDTO.getNazwa()+" - sprzedarz");
            historiaService.save(historia);
            return "redirect:/krypto?sprzedano";
        }else {
            walutaKupiona.setIlosc(kursDTO.getIlosc());

            Historia historia = new Historia();
            historia.setTyp("Kryptowaluta");
            historia.setData_start(null);

            Calendar cal = Calendar.getInstance();
            Timestamp ts2 = new Timestamp(System.currentTimeMillis());
            cal.setTime(ts2);
            historia.setData_start(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())));

            historia.setWklad(kursDTO.getSprzedarz().multiply(kursDTO.getIlosc()));
            historia.setZwrot(null);
            historia.setUser_id(user);
            historia.setNazwa(kursDTO.getNazwa()+" - zakup");
            historiaService.save(historia);

            walutaKupiona.setCzy_krypto(true);
            walutaKupionaService.save(walutaKupiona);
            return "redirect:/krypto?kupiono";
        }
    }
}
