package ru.grigan.my_blog.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import ru.grigan.my_blog.model.Role;
import ru.grigan.my_blog.model.User;
import ru.grigan.my_blog.repository.UserRepository;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final MailService mailService;

    public UserService(UserRepository repository, PasswordEncoder encoder, MailService mailService) {
        this.repository = repository;
        this.encoder = encoder;
        this.mailService = mailService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return repository.findByUsername(s);
    }

    public boolean addUser(User user) {
        User byUsername = repository.findByUsername(user.getUsername());
        if (byUsername != null) {
            return false;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        repository.save(user);
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s!\n"
                            + " Welcome to My Simple Blog!"
                            + " Please visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(), user.getActivationCode());
            mailService.sendMessage(user.getEmail(), message, "Activation code!");
        }
        return true;
    }

    public boolean isActivated(String code) {
        User user = repository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        repository.save(user);
        return true;
    }
}
