package com.example.projekt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "lokaty")
public class Lokata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bank;

    private String nazwa;

    private BigDecimal procent;

    private BigDecimal procent_po_opodatkowaniu;

    private Long okres;

    private Long minimum;

    private Long maksimum;

    private String anulowanie;

    public int getOkresInt(){
        return Math.toIntExact(okres);
    }

}
