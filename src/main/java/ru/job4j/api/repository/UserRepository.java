package ru.job4j.api.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("""
            SELECT u FROM User u
            WHERE u.username = :username AND u.passwordHash = :passwordHash
            """)
    Optional<User> findByUsernameAndPasswordHash(@Param("username") String username,
                                                 @Param("passwordHash") String passwordHash);

    @Query("""
            SELECT s.follower
            FROM Subscription s
            WHERE s.following.id = :userId
            """)
    List<User> findFollowersByUserId(@Param("userId") Long id);

    @Query("""
            SELECT fr.fromUser
            FROM FriendRequest fr
            WHERE fr.toUser.id = :userId AND fr.status = 'ACCEPTED'
            UNION
            SELECT fr.toUser
            FROM FriendRequest fr
            WHERE fr.fromUser.id = :userId AND fr.status = 'ACCEPTED'
            """)
    List<User> findFriendsByUserId(@Param("userId") Long userId);
}
