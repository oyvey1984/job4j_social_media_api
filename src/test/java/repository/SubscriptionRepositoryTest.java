package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.Job4jSocialMediaApiApplication;
import ru.job4j.api.model.Subscription;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.SubscriptionRepository;
import ru.job4j.api.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Job4jSocialMediaApiApplication.class)
@ActiveProfiles("test")
@Transactional
class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    private User follower;
    private User following;

    @BeforeEach
    void setUp() {
        subscriptionRepository.deleteAll();
        userRepository.deleteAll();

        follower = new User();
        follower.setUsername("follower");
        follower.setEmail("follower@example.com");
        follower.setPasswordHash("hashed123");
        follower = userRepository.save(follower);

        following = new User();
        following.setUsername("following");
        following.setEmail("following@example.com");
        following.setPasswordHash("hashed456");
        following = userRepository.save(following);
    }

    @Test
    void saveSubscriptionThenFindById() {
        Subscription subscription = new Subscription();
        subscription.setFollower(follower);
        subscription.setFollowing(following);
        subscription.setCreatedAt(Instant.now());

        Subscription savedSub = subscriptionRepository.save(subscription);
        Optional<Subscription> foundSub = subscriptionRepository.findById(savedSub.getId());

        assertThat(foundSub).isPresent();
        assertThat(foundSub.get().getFollower().getId()).isEqualTo(follower.getId());
        assertThat(foundSub.get().getFollowing().getId()).isEqualTo(following.getId());
    }

    @Test
    void findAllReturnsAllSubscriptions() {
        Subscription sub1 = new Subscription();
        sub1.setFollower(follower);
        sub1.setFollowing(following);
        sub1.setCreatedAt(Instant.now());

        Subscription sub2 = new Subscription();
        sub2.setFollower(following);
        sub2.setFollowing(follower);
        sub2.setCreatedAt(Instant.now());

        subscriptionRepository.save(sub1);
        subscriptionRepository.save(sub2);

        Iterable<Subscription> subscriptions = subscriptionRepository.findAll();
        assertThat(subscriptions).hasSize(2);
    }

    @Test
    void deleteSubscriptionThenNotFound() {
        Subscription subscription = new Subscription();
        subscription.setFollower(follower);
        subscription.setFollowing(following);
        subscription.setCreatedAt(Instant.now());

        Subscription savedSub = subscriptionRepository.save(subscription);
        subscriptionRepository.deleteById(savedSub.getId());

        Optional<Subscription> foundSub = subscriptionRepository.findById(savedSub.getId());
        assertThat(foundSub).isEmpty();
    }
}