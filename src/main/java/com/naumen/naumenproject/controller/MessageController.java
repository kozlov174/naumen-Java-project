package com.naumen.naumenproject.controller;

import com.naumen.naumenproject.entity.Message;
import com.naumen.naumenproject.entity.Rent;
import com.naumen.naumenproject.entity.Role;
import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.MessageRepository;
import com.naumen.naumenproject.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/rent")
public class MessageController {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private MessageRepository messageRepository;

    // Получение страницы создания комментария
    @GetMapping("/{id}/review")
    public String createMessagePage(@PathVariable Long id,
                                    @ModelAttribute(name = "message") Message message,
                                    Model model) {
        return getRentModel(model, id) == null ? "errors/404" : "rent/review";
    }

    // POST запрос на создание комментария
    @PostMapping("/{id}/review")
    public String addMessage(@PathVariable Long id,
                             @AuthenticationPrincipal User user,
                             Rent rent,
                             @Valid @ModelAttribute(name = "message") Message message,
                             BindingResult bindingResult,
                             Model model) {
        if (!bindingResult.hasErrors()) {
            String date = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
            message.setDate(date);
            message.setRent(rent);
            message.setAuthor(user);
//            message.setCommented(true);
            messageRepository.save(message);
        } else {
            return getRentModel(model, id) == null ? "errors/404" : "rent/review";
        }
        return "redirect:/rent/{id}";
    }

    // Получение страницы изменения комментария
    @GetMapping("/{id}/change-review/{msg_id}")
    public String getChangeMessagePage(@PathVariable(name = "id") Long id,
                                       @PathVariable(name = "msg_id") Long msgId,
                                       @AuthenticationPrincipal User user,
                                       Model model) {
        Optional<Message> optionalMessage = messageRepository.findById(msgId);

        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();

            if (isAccessNotAllowed(message, user)) {
                return "errors/no-access";
            }

            model = getRentModel(model, id);
            if (model != null) {
                model.addAttribute("message", message);
                return "rent/change_review";
            }
        }
        return "errors/404";
    }

    // Patch запрос на изменение комментария
    @PatchMapping("/{id}/change-review/{msg_id}")
    public String updateMessage(@PathVariable Long id,
                                @PathVariable(name = "msg_id") Long msgId,
                                @AuthenticationPrincipal User user,
                                @Valid @ModelAttribute(name = "message") Message reqMessage,
                                BindingResult bindingResult,
                                Model model) {
        if (!bindingResult.hasErrors()) {
            Optional<Message> optionalMessage = messageRepository.findById(msgId);
            if (optionalMessage.isPresent()) {
                Message message = optionalMessage.get();

                if (isAccessNotAllowed(message, user)) {
                    return "errors/no-access";
                }

                message.setMessageText(reqMessage.getMessageText());
                message.setRating(reqMessage.getRating());
                message.setChanged(true);
                messageRepository.save(message);
                return "redirect:/rent/{id}";
            }
        }
        return getRentModel(model, id) == null ? "errors/404" : "rent/review";
    }

    // Удаление комментария со страницы
    @DeleteMapping("/{id}")
    public String deleteMessage(@PathVariable Long id,
                                @RequestParam(name = "delete") Long msgId) {
        Optional<Message> msgById = messageRepository.findById(msgId);
        if (msgById.isPresent()) {
            messageRepository.deleteById(msgId);
        }
        return "redirect:/rent/{id}";
    }

    public boolean isAccessNotAllowed(Message message, User user) {
        boolean isUserAuthor = Objects.equals(message.getAuthor().getId(), user.getId());
        boolean isUserAdmin = user.getAuthorities().contains(Role.ADMIN);

        return !isUserAuthor && !isUserAdmin;
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
