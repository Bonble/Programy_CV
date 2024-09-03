package com.example.projekt.repository;

import com.example.projekt.models.LokataAktywna;
import com.example.projekt.models.User;
import com.example.projekt.repositories.LokataAktywnaRepository;
import com.example.projekt.repositories.LokataRepository;
import com.example.projekt.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@DataJpaTest
public class LokataAktywnaRepositoryTest {
    @Autowired
    private LokataAktywnaRepository lokataAktywnaRepository;

    @Autowired
    UserRepository userRepository;

    private LokataAktywna lokataAktywna1;
    private LokataAktywna lokataAktywna2;

    @BeforeEach
    public void init(){
        Date start = new Date(1717192800000L);
        Date koniec = new Date(1719698400000L);
        User u = User.builder()
                .login("a")
                .password("aa")
                .build();
        userRepository.save(u);

        lokataAktywna1 = LokataAktywna.builder()
                .bank("bank")
                .nazwa("lokata")
                .data_koniec(koniec)
                .data_start(start)
                .ilosc(1000L)
                .procent(new BigDecimal(5))
                .procent_po_opodatkowaniu(new BigDecimal(4))
                .user_id(u)
                .build();

        start = new Date(1714514400000L);
        koniec = new Date(1717106400000L);
        u = User.builder()
                .login("b")
                .password("bb")
                .build();
        userRepository.save(u);

        lokataAktywna2 = LokataAktywna.builder()
                .bank("bank2")
                .nazwa("lokata2")
                .data_koniec(koniec)
                .data_start(start)
                .ilosc(2000L)
                .procent(new BigDecimal(6))
                .procent_po_opodatkowaniu(new BigDecimal(5))
                .user_id(u)
                .build();
    }

    @Test
    public void LokataAktywnaRepository_Save_ReturnSavedLokataAktywna(){


        LokataAktywna savedLokataAktywna = lokataAktywnaRepository.save(lokataAktywna1);

        Assertions.assertThat(savedLokataAktywna).isNotNull();
        Assertions.assertThat(savedLokataAktywna.getId()).isGreaterThan(0);
        Assertions.assertThat(savedLokataAktywna.equals(lokataAktywna1)).isTrue();
    }

    @Test
    public void LokataAktywnaRepository_GetAll_ReturnMoreThanOneLokataAktywna(){


        lokataAktywnaRepository.save(lokataAktywna1);
        lokataAktywnaRepository.save(lokataAktywna2);

        List<LokataAktywna> lokataAktywnaList = lokataAktywnaRepository.findAll();

        Assertions.assertThat(lokataAktywnaList).isNotNull();
        Assertions.assertThat(lokataAktywnaList.size()).isEqualTo(2);

        Assertions.assertThat(lokataAktywnaList.get(0).equals(lokataAktywna1)).isTrue();
        Assertions.assertThat(lokataAktywnaList.get(1).equals(lokataAktywna2)).isTrue();
    }
}
