package ru.job4j.api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.api.model.Post;

public interface PostRepository extends CrudRepository<Post, Long> {
}
