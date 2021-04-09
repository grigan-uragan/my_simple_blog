package ru.grigan.my_blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import ru.grigan.my_blog.model.Message;
import ru.grigan.my_blog.model.User;
import ru.grigan.my_blog.repository.MessageRepository;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
    private final MessageRepository messageRepository;
    @Value("${upload.path}")
    private String uploadPath;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping("/save")
    public String addMessage(@AuthenticationPrincipal User user,
                             @Valid @ModelAttribute Message message,
                             BindingResult bindingResult,
                             @RequestParam (name = "file") MultipartFile file,
                             Model model) throws IOException {
        message.setAuthor(user);
        if (!bindingResult.hasErrors()) {
            saveFile(message, file);
            messageRepository.save(message);
            model.addAttribute("message", new Message());
        }
        model.addAttribute("messages", messageRepository.findAll());
        return "main";
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable User user,
                               @RequestParam(value = "message", required = false) Message message,
                               Model model) {
        if (message == null) {
            message = new Message();
        }
        Set<Message> messages = user.getMessages();
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("userChanel", user);
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String saveUserMessage(@AuthenticationPrincipal User currentUser,
                                  @PathVariable Long user,
                                  @RequestParam("id") Message message,
                                  @RequestParam("text") String text,
                                  @RequestParam("tag") String tag) {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            messageRepository.save(message);
        }
        return "redirect:/user-messages/" + user;
    }

    private void saveFile(Message message, MultipartFile file) throws IOException {
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
    }
}
