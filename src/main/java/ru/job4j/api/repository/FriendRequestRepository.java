package ru.job4j.api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.api.model.FriendRequest;
import ru.job4j.api.model.User;

import java.util.Optional;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {

    void deleteByFromUserAndToUser(User fromUser, User toUser);

    Optional<FriendRequest> findByFromUserAndToUser(User fromUser, User toUser);

    boolean existsByFromUserAndToUser(User fromUser, User toUser);
}