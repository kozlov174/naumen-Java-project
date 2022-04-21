package com.naumen.naumenproject.repository;

import com.naumen.naumenproject.entity.Notification;
import com.naumen.naumenproject.entity.RentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Iterable<Notification> findAllByAuthorIdAndStatus(Long id, RentStatus status);
    Iterable<Notification> findAllBySenderIdAndStatus(Long id, RentStatus status);
    Optional<Notification> findBySenderIdAndRentId(Long senderId, Long rentId);
}
