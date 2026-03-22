package ru.job4j.api.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.api.model.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {

    @Modifying
    @Query("DELETE FROM Image i WHERE i.post.id = :postId")
    int deleteImagesByPostId(@Param("postId") Long postId);
}
