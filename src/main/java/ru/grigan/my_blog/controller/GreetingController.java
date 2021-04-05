package ru.grigan.my_blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.grigan.my_blog.model.Message;
import ru.grigan.my_blog.repository.MessageRepository;

@Controller
public class GreetingController {
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/")
    public String helloPage(@RequestParam(name = "name", required = false, defaultValue = "Grigan")
                                        String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @GetMapping("/main")
    public String main(Model model) {
        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    @PostMapping("/save")
    public String addMessage(@RequestParam String text, @RequestParam String tag) {
        Message message = Message.of(text, tag);
        messageRepository.save(message);
        return "redirect:/main";
    }

    @PostMapping("/filter")
    public String filterMessages(@RequestParam String filter, Model model) {
        Iterable<Message> messages = null;
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepository.findByTag(filter);
        } else {
            messages = messageRepository.findAll();
        }
        model.addAttribute("messages", messages);
        return "main";
    }
}
