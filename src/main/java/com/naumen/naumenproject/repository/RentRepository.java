package com.naumen.naumenproject.repository;

import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long>, JpaSpecificationExecutor<Rent> {
    Iterable<Rent> findAllByAuthor(User user);
}
