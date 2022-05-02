package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.config.RentSpecification;
import com.naumen.naumenproject.entity.Message;
import com.naumen.naumenproject.entity.Notification;
import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.MessageRepository;
import com.naumen.naumenproject.repository.NotificationRepository;
import com.naumen.naumenproject.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.naumen.naumenproject.entity.RentStatus.REJECTED;
import static com.naumen.naumenproject.entity.RentStatus.UNDER_CONSIDERATION;

@Controller
@RequestMapping("/rent")
public class RentController {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // Отображает все предложения
    @GetMapping
    public String displayAllRents(@RequestParam MultiValueMap<String, String> params,
                                  Model model) {
        List<String> paramsList = new ArrayList<>();
        Iterable<Rent> rents;
        if (params == null) {
            rents = rentRepository.findAll();
        } else {
            System.out.println(params);
            Specification<Rent> spec = new RentSpecification(params);
            rents = rentRepository.findAll(spec);
            for (List<String> arr : params.values()) {
                paramsList.addAll(arr);
            }
        }
        model.addAttribute("params", paramsList);
        model.addAttribute("rents", rents);
        return "rent/rents";
    }

    // Получение страницы с созданием предложения
    @GetMapping("/create")
    public String createRentPage(@ModelAttribute(name = "rent") Rent rent) {
        return "rent/create_rent";
    }

    // POST запрос на создание предложения
    @PostMapping("/create")
    public String addRent(@AuthenticationPrincipal User user,
                          @Valid @ModelAttribute(name = "rent") Rent rent,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "rent/create_rent";
        }
        rent.setAuthor(user);
        rentRepository.save(rent);
        return "redirect:/rent";
    }

    // Получение страницы с информацией о жилье
    @GetMapping("/{id}")
    public String viewRentPage(@PathVariable Long id,
                               @AuthenticationPrincipal User user,
                               Model model) {

        Optional<Notification> hasNotified = notificationRepository.findBySenderIdAndRentId(user.getId(), id);
        Notification notification = hasNotified.orElseGet(Notification::new);
        model.addAttribute("notification", notification);
        return getRentPage(model, id, user);
    }

    @PostMapping("/{id}")
    public String sendNotification(@PathVariable Long id,
                                   @AuthenticationPrincipal User user,
                                   @Valid Notification notificationReq,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return getRentPage(model, id, user);
        }

        Optional<Rent> optional = rentRepository.findById(id);
        if (optional.isPresent()) {
            Rent rent = optional.get();
            saveNotification(rent, user, notificationReq);
            return "redirect:{id}";
        }
        return "errors/404";
    }

    public void saveNotification(Rent rent, User user, Notification notificationReq) {
        boolean isUserNotAuthor = !rent.getAuthor().getId().equals(user.getId());
        Long id = rent.getId();
        if (isUserNotAuthor) {
            Optional<Notification> hasNotified = notificationRepository.findBySenderIdAndRentId(user.getId(), id);
            int period = notificationReq.getPeriod();
            Notification notification;
            if (hasNotified.isEmpty()) {
                notification = new Notification(user, rent, UNDER_CONSIDERATION, rent.getAuthor(), period);
            } else {
                notification = hasNotified.get();
                boolean isRejected = notification.getStatus() == REJECTED;
                if (isRejected) {
                    notification.setStatus(UNDER_CONSIDERATION);
                    notification.setPeriod(period);
                }
            }
            notificationRepository.save(notification);
        }
    }

    public String getRentPage(Model model, Long id, User user) {
        model = getRentModel(model, id);
        if (model == null) {
            return "errors/404";
        }

        Iterable<Message> messages = messageRepository.findAllByRentId(id);
        model.addAttribute("messages", messages);
        model.addAttribute("user", user);

        return "rent/rent_page";
    }

    public Model getRentModel(Model model, Long id) {
        Optional<Rent> rentById = rentRepository.findById(id);
        if (rentById.isPresent()) {
            Rent rent = rentById.get();
            model.addAttribute("rent", rent);

            return model;
        }
        return null;
    }
}
