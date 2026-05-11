package ru.job4j.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
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

    @Transactional
    @Modifying
    @Query("""
            UPDATE User u
            SET u.username = :#{#user.username},
            u.email = :#{#user.email}
            where u.id = :#{#user.id}
            """)
    int update(@Param("user") User user);

    @Transactional
    @Modifying
    @Query("delete from User u where u.id=:pId")
    int delete(@Param("pId") Long id);

    Boolean existsByEmail(String email);

    List<User> findAllByIdIn(List<Long> ids);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
