package ru.grigan.my_blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.grigan.my_blog.model.User;
import ru.grigan.my_blog.model.dto.CaptchaResponseDto;
import ru.grigan.my_blog.service.UserService;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {
    private static final String CAPTCHA_URL =
            "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    private final UserService service;
    private final RestTemplate restTemplate;
    @Value("${recaptcha.secret}")
    private String secret;

    public RegistrationController(UserService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/reg")
    public String addUser(
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            Model model) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto responseDto =
                restTemplate.postForObject(url, Collections.EMPTY_LIST, CaptchaResponseDto.class);
        if (!responseDto.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }
        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            bindingResult.addError(new FieldError("user", "password2",
                    "different passwords"));
        }
        if (bindingResult.hasErrors() || !responseDto.isSuccess()) {
            return "registration";
        }
        if (!service.addUser(user)) {
            model.addAttribute("message", "The user with name "
                    + user.getUsername() + " already exist ");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/reg")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
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
