package ru.grigan.my_blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(String mailTo, String message, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(subject);
        mailMessage.setFrom(username);
        mailMessage.setTo(mailTo);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
