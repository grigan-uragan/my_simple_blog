package ru.grigan.my_blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.grigan.my_blog.model.Role;
import ru.grigan.my_blog.model.User;
import ru.grigan.my_blog.repository.UserRepository;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/reg")
    public String addUser(User user, Model model) {
        User byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername != null) {
            model.addAttribute("message", "The user with name "
                    + user.getUsername() + " already exist ");
            return "registration";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/reg")
    public String registrationPage() {
        return "registration";
    }
}
