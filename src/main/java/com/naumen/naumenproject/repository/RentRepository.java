package com.naumen.naumenproject.repository;

import com.naumen.naumenproject.entity.Message;
import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, Long> {
    Iterable<Rent> findAllByAuthor(User user);
}
