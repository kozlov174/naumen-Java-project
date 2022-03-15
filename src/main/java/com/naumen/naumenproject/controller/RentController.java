package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.model.Rent;
import com.naumen.naumenproject.model.User;
import com.naumen.naumenproject.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rent")
public class RentController {

    @Autowired
    private RentRepository rentRepository;

    @GetMapping
    public String getRents(Model model) {
        Iterable<Rent> rents = rentRepository.findAll();
        model.addAttribute("rents", rents);
        return "rent";
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
        return "rent";
    }

    @GetMapping("/create")
    public String createRent() {
        return "create_rent";
    }
}
