package com.naumen.naumenproject.repository;

import com.naumen.naumenproject.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, Long> {
}
