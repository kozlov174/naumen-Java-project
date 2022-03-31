package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.RentRepository;
import com.naumen.naumenproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentRepository rentRepository;

    @GetMapping("/{id}")
    public String getProfile(@PathVariable Long id,
                             @ModelAttribute Rent rent,
                             @AuthenticationPrincipal User authUser,
                             Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User userById = optionalUser.get();
            Iterable<Rent> rentsByUserId = rentRepository.findAllByAuthor(userById);
            model.addAttribute("user", userById);
            model.addAttribute("rents", rentsByUserId);
            model.addAttribute("authUser", authUser);
            return "profile/profile";
        }
        return "errors/404";
    }

    @GetMapping("/settings")
    public String changeProfilePage(@AuthenticationPrincipal User user,
                                    Model model) {
        System.out.println(user.getFirstName() + user.getLastName());
        model.addAttribute("user", user);
        return "profile/profile-settings";
    }

    @PostMapping("/settings")
    public String updateProfile(@RequestParam("firstName") String firstname,
                                @RequestParam("lastName") String lastname,
                                @AuthenticationPrincipal User user,
                                Model model) {
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            User userById = userOptional.get();
            userById.setFirstName(firstname);
            userById.setLastName(lastname);
            userRepository.save(userById);
            Long id = userById.getId();
            return String.format("redirect:/profile/%s", id);
        }
        return "errors/404";
    }
}
