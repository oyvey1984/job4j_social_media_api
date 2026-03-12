package ru.job4j.api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.api.model.Subscription;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
}