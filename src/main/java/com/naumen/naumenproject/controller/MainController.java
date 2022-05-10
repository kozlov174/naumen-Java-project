package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.naumen.naumenproject.entity.Role.ADMIN;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("title", "main");
        return "main";
    }

    @GetMapping("/about")
    public String about(@AuthenticationPrincipal User user,
                        Model model) {
        if (!user.getRoles().contains(ADMIN)) {
            return "errors/404";
        }

        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "about";
    }

    @PatchMapping("/about")
    public String setRole(@AuthenticationPrincipal User user,
                          @RequestParam(name = "userReq") User userReq,
                          @RequestParam(name = "role", required = false) String role,
                          Model model) {
        if (!user.getRoles().contains(ADMIN)) {
            return "errors/404";
        }

        if (role == null) {
            userReq.getRoles().remove(ADMIN);
        } else {
            userReq.getRoles().add(ADMIN);
        }
        userRepository.save(userReq);
        return "redirect:/about";
    }
}
