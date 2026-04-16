package ru.job4j.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.api.model.Post;
import ru.job4j.api.model.User;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);

    List<Post> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(Instant from, Instant to);

    Page<Post> findAllByOrderByCreatedAtAsc(Pageable pageable);

    @Query("""
            SELECT p
            FROM Post p
            WHERE p.user.id IN (SELECT s.follower.id
                                FROM Subscription s
                                WHERE s.following.id = :userId)
            ORDER BY p.createdAt DESC
            """)
    Page<Post> findPostsBySubscribers(@Param("userId") Long userid, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("""
            DELETE FROM Post p
            WHERE p.id = :postId
            """)
    int deleteByPostId(@Param("postId") Long postId);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Post p
            SET p.title = :newTitle, p.content = :newContent, p.updatedAt = CURRENT_TIMESTAMP
            WHERE p.id = :id
            """)
    int updateTitleAndContentByPostId(@Param("newTitle") String title,
                                       @Param("newContent") String content,
                                       @Param("id") Long id);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
    List<Post> findByUserIds(@Param("userIds") List<Long> userIds);
}
