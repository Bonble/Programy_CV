package com.example.projekt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "historia")
public class Historia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    private String typ;

    private String nazwa;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date data_start;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date data_koniec;

    private BigDecimal wklad;

    private BigDecimal zwrot;
}
