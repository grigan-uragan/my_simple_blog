package ru.grigan.my_blog.repository;

import org.springframework.data.repository.CrudRepository;
import ru.grigan.my_blog.model.Message;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findByTag(String tag);
}
