package com.example.projekt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "walutyKupione")
public class WalutaKupiona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    private String nazwa;
    @Column(name = "ilosc", precision = 16, scale = 5)
    private BigDecimal ilosc; // zrobić BigDecimal

    private boolean czy_krypto;

    public boolean getCzy_krypto(){
        return this.czy_krypto;
    }
}
