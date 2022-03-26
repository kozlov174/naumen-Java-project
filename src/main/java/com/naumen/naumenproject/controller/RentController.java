package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.entity.Message;
import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.MessageRepository;
import com.naumen.naumenproject.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

@Controller
@RequestMapping("/rent")
public class RentController {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private MessageRepository messageRepository;

    // Отображает все предложения
    @GetMapping
    public String displayAllRents(Model model) {
        Iterable<Rent> rents = rentRepository.findAll();
        model.addAttribute("rents", rents);
        return "rents";
    }

    // Получение страницы с созданием предложения
    @GetMapping("/create")
    public String getCreateRentPageInfo() {
        return "create_rent";
    }

    // POST запрос на создание предложения
    @PostMapping
    public String addRent(@AuthenticationPrincipal User user,
                          @RequestParam String title,
                          @RequestParam String description,
                          @RequestParam String houseType,
                          Model model) {

        Rent rent = new Rent(title, description, houseType, user);

        rentRepository.save(rent);

        Iterable<Rent> rents = rentRepository.findAll();
        model.addAttribute("rents", rents);
        return "rents";
    }

    // Получение страницы с информацией о жилье
    @GetMapping("/{id}")
    public String viewRent(
            @PathVariable Long id,
            Model model) {
        return getRentPageInfo(model, id);
    }

    // Удаление комментария со страницы
    @PostMapping("/{id}")
    public String deleteMessageReview(@PathVariable Long id,
                                      @RequestParam(name = "delete") Long msgId) {
        Optional<Message> msgById = messageRepository.findById(msgId);
        if (msgById.isPresent()) {
            messageRepository.deleteById(msgId);
        }
        return "redirect:/rent/{id}";
    }

    // Получение страницы создания комментария
    @GetMapping("/{id}/review")
    public String getMessageReviewPage(@PathVariable Long id,
                                       @ModelAttribute(name = "messageForm") Message message,
                                       Model model) {
        return getMessageReviewPageInfo(model, id);
    }

    // POST запрос на создание комментария
    @PostMapping("/{id}/review")
    public String addMessageReview(@PathVariable Long id,
                                   @AuthenticationPrincipal User user,
                                   Rent rent,
                                   @Valid @ModelAttribute(name = "messageForm") Message message,
                                   BindingResult bindingResult,
                                   Model model) {

        if (!bindingResult.hasErrors()) {
            String date = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
            message.setDate(date);
            message.setRent(rent);
            message.setAuthor(user);
            messageRepository.save(message);
        } else {
            return getMessageReviewPageInfo(model, id);
        }
        return "redirect:/rent/{id}";
    }

    public String getRentPageInfo(Model model, Long id) {
        Optional<Rent> rentById = rentRepository.findById(id);
        if (rentById.isPresent()) {
            Rent rent = rentById.get();
            model.addAttribute("rent", rent);

            Iterable<Message> messages = messageRepository.findAllByRentId(id);
            model.addAttribute("messages", messages);
            return "rent_page";
        }
        return "404";
    }

    public String getMessageReviewPageInfo(Model model, Long id) {
        Optional<Rent> rentById = rentRepository.findById(id);
        if (rentById.isPresent()) {
            Rent rent = rentById.get();
            model.addAttribute("rent", rent);

            return "review";
        }
        return "404";
    }
}
