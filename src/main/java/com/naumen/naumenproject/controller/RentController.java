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
        System.out.println(rent.getPrice());
        if (bindingResult.hasErrors()) {
            return "rent/create_rent";
        }

        rent.setAuthor(user);
        rentRepository.save(rent);

        return "redirect:/rent";
    }

    // Получение страницы с информацией о жилье
    @GetMapping("/{id}")
    public String viewRent(@PathVariable Long id,
                           Model model) {

        model = getRentModel(model, id);
        if (model == null) {
            return "errors/404";
        }

        Iterable<Message> messages = messageRepository.findAllByRentId(id);
        model.addAttribute("messages", messages);

        return "rent/rent_page";
    }

    // Удаление комментария со страницы
    @PostMapping("/{id}")
    public String deleteMessage(@PathVariable Long id,
                                @RequestParam(name = "delete") Long msgId) {
        Optional<Message> msgById = messageRepository.findById(msgId);
        if (msgById.isPresent()) {
            messageRepository.deleteById(msgId);
        }
        return "redirect:/rent/{id}";
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
