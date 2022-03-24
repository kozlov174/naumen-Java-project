package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.model.User;
import com.naumen.naumenproject.service.RegistrationService;
import com.naumen.naumenproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;

    @GetMapping
    public String registration(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        return userService.signUpUser(user);
    }
}
