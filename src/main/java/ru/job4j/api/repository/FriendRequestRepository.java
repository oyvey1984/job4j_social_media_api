package ru.job4j.api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.api.model.FriendRequest;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {
}