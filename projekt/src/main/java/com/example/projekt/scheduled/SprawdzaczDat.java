package com.example.projekt.scheduled;

import com.example.projekt.models.Historia;
import com.example.projekt.models.LokataAktywna;
import com.example.projekt.models.User;
import com.example.projekt.services.HistoriaService;
import com.example.projekt.services.LokataAktywnaService;
import com.example.projekt.services.UserServiceImpl;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Component
public class SprawdzaczDat {
    @Autowired
    LokataAktywnaService lokataAktywnaService;

    @Autowired
    HistoriaService historiaService;

    public void sprawdzanieLokat(){
        List<LokataAktywna> lokataAktywnaList = (List<LokataAktywna>) lokataAktywnaService.getAll();

        Calendar cal = Calendar.getInstance();
        Timestamp ts2 = new Timestamp(System.currentTimeMillis());
        cal.setTime(ts2);

        for(LokataAktywna lokataAktywna: lokataAktywnaList){
            if(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())).equals(lokataAktywna.getData_koniec())){
                Historia historia = new Historia();
                historia.setUser_id(lokataAktywna.getUser_id());
                historia.setNazwa(lokataAktywna.getBank()+" - "+lokataAktywna.getNazwa());
                historia.setTyp("Lokata");
                historia.setWklad(BigDecimal.valueOf(lokataAktywna.getIlosc()));
                historia.setData_koniec(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())));
                historia.setData_start(lokataAktywna.getData_start());

                Calendar start = Calendar.getInstance();
                start.setTime(lokataAktywna.getData_start());

                int startDzienMies = start.get(Calendar.DAY_OF_MONTH);
                int startMies = 12 * start.get(Calendar.YEAR)+ start.get(Calendar.MONTH);

                Calendar koniec = Calendar.getInstance();
                koniec.setTime(lokataAktywna.getData_koniec());

                int koniecDzienMies= koniec.get(Calendar.DAY_OF_MONTH);
                int koniecMies= 12 * koniec.get(Calendar.YEAR)+ koniec.get(Calendar.MONTH);
                int okres;
                if(startDzienMies > koniecDzienMies){
                    okres = koniecMies - startMies -1;
                }
                else {
                    okres = koniecMies-startMies;
                }
                historia.setZwrot(BigDecimal.valueOf((float)lokataAktywna.getIlosc()+
                        (((float)lokataAktywna.getIlosc()
                                *((lokataAktywna.getProcent().floatValue()/100)/12))
                                *(float)okres)));
                lokataAktywnaService.delete(lokataAktywna);
                historiaService.save(historia);
            }
        }


    }
}
