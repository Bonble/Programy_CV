package com.example.projekt.scheduled;

import com.example.projekt.models.LokataAktywna;
import com.example.projekt.services.HistoriaService;
import com.example.projekt.services.LokataAktywnaService;
import com.example.projekt.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class Maintenance {
    @Autowired
    SprawdzaczDat sprawdzaczDat;


    @Scheduled(cron = "0 0 1 ? * *")
    public void maintON(){
        StatusStrony  statusStrony = StatusStrony.getInstance();
        statusStrony.setMaintenance(true);
        System.out.println("maintence mode: ON");
        Scraper scraper = Scraper.getInstance();
        scraper.run();

        sprawdzaczDat.sprawdzanieLokat();
    }

    @Scheduled(cron = "0 5 1 ? * *")
    public void maintOFF(){
        StatusStrony  statusStrony = StatusStrony.getInstance();
        statusStrony.setMaintenance(false);
        System.out.println("maintence mode: OFF");
    }
}
