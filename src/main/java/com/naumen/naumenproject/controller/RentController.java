package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.model.Rent;
import com.naumen.naumenproject.model.User;
import com.naumen.naumenproject.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/rent")
public class RentController {

    @Autowired
    private RentRepository rentRepository;

    @GetMapping
    public String getRents(Model model) {
        Iterable<Rent> rents = rentRepository.findAll();
        model.addAttribute("rents", rents);
        return "rents";
    }

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

    @GetMapping("/create")
    public String createRent() {
        return "create_rent";
    }

    @GetMapping("/{id}")
    public String viewRent(@PathVariable Long id, Model model) {
        Optional<Rent> optional = rentRepository.findById(id);
        if (optional.isPresent()) {
            Rent rent = optional.get();
            model.addAttribute("rent", rent);
            return "rent_page";
        }
        return "404";
    }
}
