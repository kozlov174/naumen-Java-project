package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.model.User;
import com.naumen.naumenproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String about(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "about";
    }
}
