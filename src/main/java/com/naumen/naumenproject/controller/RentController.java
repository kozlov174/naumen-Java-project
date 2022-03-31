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
import org.springframework.util.MultiValueMap;
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
    public String displayAllRents(@RequestParam MultiValueMap<String, String> filters,
                                  Model model) {
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
                           @AuthenticationPrincipal User user,
                           Model model) {

        model = getRentModel(model, id);
        if (model == null) {
            return "errors/404";
        }

        Iterable<Message> messages = messageRepository.findAllByRentId(id);
        model.addAttribute("messages", messages);
        model.addAttribute("user", user);

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

    // Страница с настройками аренды
    @GetMapping("/{id}/settings")
    public String getRentSettings(@PathVariable Long id,
                                  @AuthenticationPrincipal User authUser,
                                  Model model) {
        Optional<Rent> optionalRent = rentRepository.findById(id);
        if (optionalRent.isPresent()) {
            Rent rent = optionalRent.get();
            boolean isAccessRestricted = rent.isAccessRestricted(authUser);
            if (isAccessRestricted) {
                model.addAttribute("rent", rent);
                return "/rent/rent-settings";
            } else {
                return "errors/no-access";
            }
        }
        return "errors/404";
    }

    // Изменение данных об аренде
    @PostMapping("/{id}/settings")
    public String updateRent(@PathVariable Long id,
                             @AuthenticationPrincipal User authUser,
                             @Valid Rent reqRent,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "rent/rent-settings";
        }

        Optional<Rent> optionalRent = rentRepository.findById(id);
        if (optionalRent.isPresent()) {
            Rent rent = optionalRent.get();
            rent.setTitle(reqRent.getTitle());
            rent.setDescription(reqRent.getDescription());
            rent.setStreet(reqRent.getStreet());
            rent.setPrice(reqRent.getPrice());

            rentRepository.save(rent);
            return "redirect:/rent/{id}";
        }
        return "errors/404";
    }

    // Удалить объявление
    @PostMapping("/{id}/settings/delete")
    public String deleteRent(@PathVariable Long id,
                             @AuthenticationPrincipal User authUser,
                             Model model) {

        Optional<Rent> optionalRent = rentRepository.findById(id);
        if (optionalRent.isPresent()) {
            Rent rent = optionalRent.get();
            boolean isAccessRestricted = rent.isAccessRestricted(authUser);
            if (isAccessRestricted) {
                rentRepository.deleteById(rent.getId());
                return "redirect:/rent";
            }
            return "errors/no-access";
        }
        return "errors/404";
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
