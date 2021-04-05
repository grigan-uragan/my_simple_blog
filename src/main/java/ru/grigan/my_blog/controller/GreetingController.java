package ru.grigan.my_blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/hello")
    public String helloPage(@RequestParam(name = "name", required = false, defaultValue = "Grigan")
                                        String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @GetMapping
    public String main(Model model) {
        model.addAttribute("some", "Hello world");
        return "main";
    }
}
