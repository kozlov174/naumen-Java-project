package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.naumen.naumenproject.entity.Role.ADMIN;

@Controller
public class ApplicationController {

    @Autowired
    private RentRepository rentRepository;

    @GetMapping("/applications")
    public String applications(@AuthenticationPrincipal User user,
                               Model model) {
        if (!user.getRoles().contains(ADMIN)) {
            return "errors/404";
        }

        Iterable<Rent> rents = rentRepository.findAllByApproved(false);
        model.addAttribute("rents", rents);
        return "applications";
    }

    @PatchMapping("/applications")
    public String patchApplication(@RequestParam("id") Rent rent,
                                   @AuthenticationPrincipal User user,
                                   Model model) {
        if (!user.getRoles().contains(ADMIN)) {
            return "errors/404";
        }
        rent.setApproved(true);
        rentRepository.save(rent);
        return "redirect:applications";
    }

    @DeleteMapping("/applications")
    public String deleteApplication(@RequestParam("id") Rent rent,
                                    @AuthenticationPrincipal User user,
                                    Model model) {
        if (!user.getRoles().contains(ADMIN)) {
            return "errors/404";
        }

        rentRepository.delete(rent);
        return "redirect:applications";
    }

}
