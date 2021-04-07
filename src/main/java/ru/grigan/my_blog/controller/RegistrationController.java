package ru.grigan.my_blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.grigan.my_blog.model.Role;
import ru.grigan.my_blog.model.User;
import ru.grigan.my_blog.repository.UserRepository;
import ru.grigan.my_blog.service.UserService;

import java.util.Collections;

@Controller
public class RegistrationController {
    private final UserService service;

    public RegistrationController(UserService service) {
        this.service = service;
    }

    @PostMapping("/reg")
    public String addUser(User user, Model model) {
        if (!service.addUser(user)) {
            model.addAttribute("message", "The user with name "
                    + user.getUsername() + " already exist ");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/reg")
    public String registrationPage() {
        return "registration";
    }

    @GetMapping("/activate/{code}")
    public String activation(@PathVariable(name = "code") String code, Model model) {
        boolean activateUser = service.isActivated(code);
        if (activateUser) {
            model.addAttribute("message", "User successfully activated!");
        } else {
            model.addAttribute("message", "Activation code not found!");
        }
        return "login";
    }
}
