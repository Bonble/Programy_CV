package com.example.projekt.repositories;

import com.example.projekt.models.KursGBP;
import com.example.projekt.models.Lokata;
import com.example.projekt.models.User;
import com.example.projekt.models.WalutaKupiona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalutaKupionaRepository extends JpaRepository<WalutaKupiona, Integer> {
}
