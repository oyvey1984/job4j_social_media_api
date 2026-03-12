package ru.job4j.api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.api.model.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
}