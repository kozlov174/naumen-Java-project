package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.RentRepository;
import com.naumen.naumenproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentRepository rentRepository;

    @GetMapping("{id}")
    public String getProfile(@PathVariable Long id,
                             @ModelAttribute Rent rent,
                             Model model) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            User user = userById.get();
            Iterable<Rent> rentsByUserId = rentRepository.findAllByAuthor(user);
            model.addAttribute("user", user);
            model.addAttribute("rents", rentsByUserId);
            return "profile/profile";
        }
        return "404";
    }
}
