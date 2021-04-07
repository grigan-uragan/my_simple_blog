package ru.grigan.my_blog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.grigan.my_blog.model.Role;
import ru.grigan.my_blog.model.User;
import ru.grigan.my_blog.service.UserService;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public String allUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("{user}")
    public String editUser(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public String saveUser(@RequestParam String username,
                           @RequestParam Map<String, String> form,
                           @RequestParam("userId") User user) {
       userService.saveUser(user, username, form);
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String email,
                                @RequestParam String password) {
        userService.updateUser(user, email, password);
        return "redirect:/user/profile";
    }
}
