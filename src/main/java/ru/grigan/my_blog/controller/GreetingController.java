package ru.grigan.my_blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.grigan.my_blog.model.Message;
import ru.grigan.my_blog.repository.MessageRepository;

@Controller
public class GreetingController {
    private final MessageRepository messageRepository;

    public GreetingController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String helloPage(@RequestParam(name = "name", required = false,
            defaultValue = "Grigan")
                                        String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepository.findByTag(filter);
        } else {
            messages = messageRepository.findAll();
        }
        model.addAttribute("message", new Message());
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }
}
