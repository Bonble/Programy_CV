package com.example.projekt.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LokataAktywnaDto {
    private Long id;
    private Long ilosc;
    private String bank;
    private String nazwa;
    private Long okres;
    private BigDecimal procent;
    private BigDecimal procent_po_opodatkowaniu;
    private String anulowanie;
    public int getOkresInt(){
        return Math.toIntExact(okres);
    }
}
