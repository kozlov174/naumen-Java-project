package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.entity.Notification;
import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.NotificationRepository;
import com.naumen.naumenproject.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;
import java.util.Optional;

import static com.naumen.naumenproject.entity.RentStatus.*;

@Controller
public class NotificationController {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/notifications")
    public String getNotifications(@AuthenticationPrincipal User user,
                                   Model model) {
        Iterable<Notification> notifications = notificationRepository.findAllByAuthorIdAndStatus(user.getId(), UNDER_CONSIDERATION);
        getNotificationsModel(model, notifications);
        return "profile/notifications";
    }

    @PostMapping("/notifications")
    public String acceptNotification(Notification notificationReq,
                                     @AuthenticationPrincipal User user,
                                     Model model) {
        Iterable<Notification> notifications = notificationRepository.findAllByAuthorIdAndStatus(user.getId(), APPROVED);
        if (notifications.spliterator().estimateSize() > 0) {
            model.addAttribute("err", "Невозможно принять более 1 бронирования");
            getNotificationsModel(model, notifications);
            return "profile/notifications";
        }

        Optional<Notification> optional = getNotificationOptional(notificationReq);
        if (optional.isPresent()) {
            Notification notification = optional.get();

            Rent rent = notification.getRent();
            rent.setCurrentRenter(notification.getSender());
            rentRepository.save(rent);

            notification.setStatus(APPROVED);
            notificationRepository.save(notification);
        }
        return "redirect:notifications";
    }

    @PatchMapping("/notifications")
    public String rejectNotification(Notification notification) {
        patchNotification(notification);
        return "redirect:notifications";
    }

    @GetMapping("/tenants")
    public String getTenants(@AuthenticationPrincipal User user, Model model) {
        Iterable<Notification> notifications = notificationRepository.findAllByAuthorIdAndStatus(user.getId(), APPROVED);
        getNotificationsModel(model, notifications);
        return "profile/tenants";
    }

    @PatchMapping("/tenants")
    public String rejectTenant(Notification notification) {
        patchNotification(notification);
        return "redirect:tenants";
    }

    @GetMapping("/landlords")
    public String getLandlord(@AuthenticationPrincipal User user, Model model) {
        Iterable<Notification> notifications = notificationRepository.findAllBySenderIdAndStatus(user.getId(), APPROVED);
        getNotificationsModel(model, notifications);
        return "profile/landlords";
    }

    @PatchMapping("/landlords")
    public String rejectLandlordTenant(Notification notification) {
        patchNotification(notification);
        return "redirect:landlords";
    }

    public void patchNotification(Notification notificationReq) {
        Optional<Notification> optional = getNotificationOptional(notificationReq);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            notification.setStatus(REJECTED);
            Rent rent = notification.getRent();
            if (Objects.equals(rent.getCurrentRenter().getId(), notification.getSender().getId())) {
                rent.setCurrentRenter(null);
                rentRepository.save(rent);
            }
            notificationRepository.save(notification);
        }
    }

    public void getNotificationsModel(Model model, Iterable<Notification> notifications) {
        model.addAttribute("notificationsSize", notifications.spliterator().estimateSize());
        model.addAttribute("notifications", notifications);
    }

    public Optional<Notification> getNotificationOptional(Notification notification) {
        return notificationRepository.findById(notification.getId());
    }

}
