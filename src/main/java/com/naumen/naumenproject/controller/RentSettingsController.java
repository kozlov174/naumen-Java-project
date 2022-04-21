package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/rent")
public class RentSettingsController {

    @Autowired
    public RentRepository rentRepository;

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
            }
            return "errors/no-access";
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
}
