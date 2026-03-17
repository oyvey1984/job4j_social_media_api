package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.Job4jSocialMediaApiApplication;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Job4jSocialMediaApiApplication.class)
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void whenSaveUserthenFindById() {
        User user = new User();
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setPasswordHash("hashed123");

        User savedUser = userRepository.save(user);
        var foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("john_doe");
        assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void whenFindAllthenReturnAllUsers() {
        User user1 = new User();
        user1.setUsername("john_doe");
        user1.setEmail("john@example.com");
        user1.setPasswordHash("hashed123");

        User user2 = new User();
        user2.setUsername("jane_doe");
        user2.setEmail("jane@example.com");
        user2.setPasswordHash("hashed456");

        userRepository.save(user1);
        userRepository.save(user2);

        var users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void whenDeleteUserthenNotFound() {
        User user = new User();
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setPasswordHash("hashed123");

        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getId());

        var foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isEmpty();
    }
}