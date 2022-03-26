package com.naumen.naumenproject.service;

import com.naumen.naumenproject.entity.User;
import com.naumen.naumenproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.naumen.naumenproject.entity.Role.USER;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Пользователь с такой почтой не найден"));
    }

    public String signUpUser(User user) {
        boolean userExists = userRepository
                .findByEmail(user.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException("Пользователь с такой почтой уже существует");
        }


        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        System.out.println(encodedPassword.length());
        user.setPassword(encodedPassword);

        user.setActive(true);
        user.setRoles(Collections.singleton(USER));
        userRepository.save(user);

        return "redirect:/login";
    }
}
