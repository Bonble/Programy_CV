package com.example.projekt.controllers;

import com.example.projekt.models.AdminHelper;
import com.example.projekt.models.LokataAktywnaDto;
import com.example.projekt.scheduled.Scraper;
import com.example.projekt.scheduled.StatusStrony;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public ModelAndView admin(AdminHelper adminHelper){
        ModelAndView modelAndView = new ModelAndView("admin");
        StatusStrony statusStrony = StatusStrony.getInstance();
        modelAndView.addObject("mainteneceAdmin", statusStrony.getMainteneceAdmin());
        return modelAndView;
    }

    @PostMapping("/adminMaintenance")
    public String adminMaintenance(Model model) {
        StatusStrony statusStrony = StatusStrony.getInstance();
        if(statusStrony.getMainteneceAdmin()){
            statusStrony.setMaintenanceAdmin(false);
        } else{
            statusStrony.setMaintenanceAdmin(true);
        }
        return "redirect:/admin";
    }

    @PostMapping("/adminWczytaj")
    public String adminWczytaj(@Valid AdminHelper adminHelper, Model model) {
        StatusStrony statusStrony = StatusStrony.getInstance();

        Scraper scraper = Scraper.getInstance();
        if (adminHelper.krypto){
            scraper.krypto();
        } if (adminHelper.lokaty){
            scraper.lokaty();
        } if (adminHelper.waluty){
            scraper.waluty();
        }

        return "redirect:/admin?wykonano";
    }

    @GetMapping("/wczytajWaluty")
    public ModelAndView wczytajWaluty() {
        ModelAndView modelAndView = new ModelAndView("admin");
        StatusStrony statusStrony = StatusStrony.getInstance();

        Scraper scraper = Scraper.getInstance();
        scraper.waluty();

        modelAndView.addObject("mainteneceAdmin", statusStrony.getMainteneceAdmin());
        return modelAndView;
    }

    @GetMapping("/wczytajKrypto")
    public ModelAndView wczytajKrypto() {
        ModelAndView modelAndView = new ModelAndView("admin");
        StatusStrony statusStrony = StatusStrony.getInstance();

        Scraper scraper = Scraper.getInstance();
        scraper.krypto();

        modelAndView.addObject("mainteneceAdmin", statusStrony.getMainteneceAdmin());
        return modelAndView;
    }

}
