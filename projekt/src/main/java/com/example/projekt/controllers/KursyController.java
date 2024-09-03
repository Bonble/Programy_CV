package com.example.projekt.controllers;

import com.example.projekt.models.*;
import com.example.projekt.scheduled.StatusStrony;
import com.example.projekt.services.HistoriaService;
import com.example.projekt.services.KursyService;
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
public class KursyController {
    @Autowired
    private KursyService kursyService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalutaKupionaService walutaKupionaService;

    @Autowired
    private HistoriaService historiaService;

    @GetMapping("/kursy")
    public ModelAndView kursy(LokataAktywnaDto lokataAktywna) {
        StatusStrony statusStrony = StatusStrony.getInstance();
        if(statusStrony.getMaintenece() || statusStrony.getMainteneceAdmin()){
            return new ModelAndView("maintenance");
        }
        ModelAndView modelAndView = new ModelAndView("kursy");
        System.out.println(kursyService.getCHFNewest().isPresent()+" "+ kursyService.getCHFNewest().isEmpty());
        if (kursyService.getCHFNewest().isPresent() &&
        kursyService.getEURNewest().isPresent() &&
        kursyService.getGBPNewest().isPresent() &&
        kursyService.getUSDNewest().isPresent()) {
            System.out.println("hehehehe");
            int id = 0;

            List<Kurs> kursyRok = kursyService.getUSDAll();
            KursDTO kursUSD = new KursDTO("USD");
            kursUSD.setDane(kursyRok);
            kursUSD.setId((long) id++);

            List<BigDecimal> srednie = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednie.add(z.getKupno().add(z.getSprzedarz()).divide(new BigDecimal(2)));
            }
            Gson gson = new Gson();
            Collections.reverse(srednie);
            String kursyJsonUSD = gson.toJson(srednie);

            kursyRok = kursyService.getCHFAll();
            KursDTO kursCHF = new KursDTO("CHF");
            kursCHF.setDane(kursyRok);
            kursCHF.setId((long) id++);

            srednie = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednie.add(z.getKupno().add(z.getSprzedarz()).divide(new BigDecimal(2)));
            }
            gson = new Gson();
            Collections.reverse(srednie);
            String kursyJsonCHF = gson.toJson(srednie);

            kursyRok = kursyService.getGBPAll();
            KursDTO kursGBP = new KursDTO("GBP");
            kursGBP.setDane(kursyRok);
            kursGBP.setId((long) id++);

            srednie = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednie.add(z.getKupno().add(z.getSprzedarz()).divide(new BigDecimal(2)));
            }
            gson = new Gson();
            Collections.reverse(srednie);
            String kursyJsonGBP = gson.toJson(srednie);

            kursyRok = kursyService.getEURAll();
            KursDTO kursEUR = new KursDTO("EUR");
            kursEUR.setDane(kursyRok);
            kursEUR.setId((long) id++);

            srednie = new ArrayList<>();
            for (Kurs z : kursyRok) {
                srednie.add(z.getKupno().add(z.getSprzedarz()).divide(new BigDecimal(2)));
            }
            gson = new Gson();
            Collections.reverse(srednie);
            String kursyJsonEUR = gson.toJson(srednie);

            List<KursDTO> kursy = new LinkedList<>();
            kursy.add(kursUSD);
            kursy.add(kursEUR);
            kursy.add(kursGBP);
            kursy.add(kursCHF);

            List<WalutaKupiona> walutaKupionaList = walutaKupionaService.getAll();
            Collection auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            ;
            String rola = auth.toString();
            if (rola.equals("[ROLE_USER]")) {
                modelAndView = new ModelAndView("kursy_user");
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
            modelAndView.addObject("usd", kursyJsonUSD);
            modelAndView.addObject("chf", kursyJsonCHF);
            modelAndView.addObject("gbp", kursyJsonGBP);
            modelAndView.addObject("eur", kursyJsonEUR);

            modelAndView.addObject("walutaItems", kursy);
        }
        return modelAndView;
    }

    @PostMapping("/operacjaWaluty")
    public String operacjaWaluty(@Valid KursDTO kursDTO, Model model) {
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
                        return "redirect:/kursy?zaDuzo";
                    }
                    else{   jest=true;
                        break;}
                }
            }
            if(!jest){
                return "redirect:/kursy?zaDuzo";
            }
            walutaKupiona.setCzy_krypto(false);
            walutaKupionaService.save(walutaKupiona);

            Historia historia = new Historia();
            historia.setTyp("Waluta");

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
            return "redirect:/kursy?sprzedano";
        }else {
            walutaKupiona.setIlosc(kursDTO.getIlosc());

            Historia historia = new Historia();
            historia.setTyp("Waluta");
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

            walutaKupiona.setCzy_krypto(false);
            walutaKupionaService.save(walutaKupiona);
            return "redirect:/kursy?kupiono";
        }
    }
}
