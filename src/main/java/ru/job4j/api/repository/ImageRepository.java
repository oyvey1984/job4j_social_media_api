package ru.job4j.api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.api.model.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {
}