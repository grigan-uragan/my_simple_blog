package ru.grigan.my_blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.grigan.my_blog.model.Message;
import ru.grigan.my_blog.model.User;
import ru.grigan.my_blog.repository.MessageRepository;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class GreetingController {
    private final MessageRepository messageRepository;
    @Value("${upload.path}")
    private String uploadPath;

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

    @PostMapping("/save")
    public String addMessage(@AuthenticationPrincipal User user,
                             @Valid @ModelAttribute Message message,
                             BindingResult bindingResult,
                             @RequestParam (name = "file")MultipartFile file,
                             Model model) throws IOException {
        message.setAuthor(user);
        if (!bindingResult.hasErrors()) {
            if (file != null && !file.isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuid = UUID.randomUUID().toString();
                String filename = uuid + "." + file.getOriginalFilename();
                message.setFilename(filename);
                file.transferTo(new File(uploadPath + "/" + filename));
            }
            messageRepository.save(message);
            model.addAttribute("message", new Message());
        }

        model.addAttribute("messages", messageRepository.findAll());
        return "main";
    }
}
