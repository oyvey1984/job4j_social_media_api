package ru.job4j.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.api.model.Post;
import ru.job4j.api.model.User;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);

    List<Post> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(Instant from, Instant to);

    Page<Post> findAllByOrderByCreatedAtAsc(Pageable pageable);

}
