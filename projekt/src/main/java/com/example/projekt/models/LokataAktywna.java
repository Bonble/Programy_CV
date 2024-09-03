package com.example.projekt.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lokatyAktywne")
public class LokataAktywna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user_id;

    private String bank;

    private String nazwa;

    private Long ilosc;

    private BigDecimal procent;

    private BigDecimal procent_po_opodatkowaniu;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date data_start;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date data_koniec;

    private String anulowanie;
}
