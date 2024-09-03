package com.example.projekt.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class KursDTO {
    private Long id;
    private String nazwa;
    private BigDecimal kupno;
    private BigDecimal sprzedarz;
    private BigDecimal trend_dzien;
    private BigDecimal trend_tydzien;
    private BigDecimal trend_miesiac;

    private BigDecimal ilosc;

    public KursDTO(String nazwa){
        this.nazwa = nazwa;
    }
    public void setDane(List<Kurs> kursy) {
        Iterable <Kurs> k1 = kursy;
        Iterator <Kurs> k1i = k1.iterator();

        BigDecimal srednia_teraz;
        Kurs z = k1i.next();
        srednia_teraz = z.getKupno().add(z.getSprzedarz()).divide(new BigDecimal(2));
        this.kupno = z.getKupno();
        this.sprzedarz = z.getSprzedarz();
        z = k1i.next();
        BigDecimal
                x1= z.getKupno().add(z.getSprzedarz()).divide(new BigDecimal(2));
        this.trend_dzien = x1.subtract(srednia_teraz).divide(srednia_teraz, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        for(int i=0; i<6; i++){
            z= k1i.next();
        }

        x1= z.getKupno().add(z.getSprzedarz()).divide(new BigDecimal(2));
        trend_tydzien = (((x1)).subtract(srednia_teraz)).divide(srednia_teraz, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));

        for(int i=0; i<24; i++){
            z= k1i.next();
        }

        x1= z.getKupno().add(z.getSprzedarz()).divide(new BigDecimal(2));
        trend_miesiac = (((x1)).subtract(srednia_teraz)).divide(srednia_teraz, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));

    }

}
