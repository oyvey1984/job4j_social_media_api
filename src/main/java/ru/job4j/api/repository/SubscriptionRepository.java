package ru.job4j.api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.api.model.Subscription;
import ru.job4j.api.model.User;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    void deleteByFollowerAndFollowing(User follower, User following);

    boolean existsByFollowerAndFollowing(User follower, User following);
}