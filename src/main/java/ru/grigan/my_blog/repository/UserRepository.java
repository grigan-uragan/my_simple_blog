package ru.grigan.my_blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.grigan.my_blog.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
