package com.example.projekt.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Kurs {
    private BigDecimal kupno;
    private BigDecimal sprzedarz;
}
