package com.example.projekt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "kursCHF")
public class KursCHF extends Kurs{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sprzedarz", precision = 16, scale = 4)
    private BigDecimal sprzedarz;
    @Column(name = "kupno", precision = 16, scale = 4)
    private BigDecimal kupno;

}
