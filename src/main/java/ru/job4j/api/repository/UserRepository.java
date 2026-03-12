package ru.job4j.api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.api.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
