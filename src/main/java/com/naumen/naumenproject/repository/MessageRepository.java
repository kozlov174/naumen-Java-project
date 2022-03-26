package com.naumen.naumenproject.repository;

import com.naumen.naumenproject.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Iterable<Message> findAllByRentId(Long id);
}
