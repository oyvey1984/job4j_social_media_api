package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.Job4jSocialMediaApiApplication;
import ru.job4j.api.model.FriendRequest;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.FriendRequestRepository;
import ru.job4j.api.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Job4jSocialMediaApiApplication.class)
@ActiveProfiles("test")
@Transactional
class FriendRequestRepositoryTest {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User fromUser;
    private User toUser;

    @BeforeEach
    void setUp() {
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();

        fromUser = new User();
        fromUser.setUsername("fromuser");
        fromUser.setEmail("from@example.com");
        fromUser.setPasswordHash("hashed123");
        fromUser = userRepository.save(fromUser);

        toUser = new User();
        toUser.setUsername("touser");
        toUser.setEmail("to@example.com");
        toUser.setPasswordHash("hashed456");
        toUser = userRepository.save(toUser);
    }

    @Test
    void saveFriendRequestThenFindById() {
        FriendRequest request = new FriendRequest();
        request.setFromUser(fromUser);
        request.setToUser(toUser);
        request.setStatus("PENDING");
        request.setCreatedAt(Instant.now());
        request.setUpdatedAt(Instant.now());

        FriendRequest savedRequest = friendRequestRepository.save(request);
        Optional<FriendRequest> foundRequest = friendRequestRepository.findById(savedRequest.getId());

        assertThat(foundRequest).isPresent();
        assertThat(foundRequest.get().getFromUser().getId()).isEqualTo(fromUser.getId());
        assertThat(foundRequest.get().getToUser().getId()).isEqualTo(toUser.getId());
        assertThat(foundRequest.get().getStatus()).isEqualTo("PENDING");
    }

    @Test
    void findAllReturnsAllRequests() {
        FriendRequest request1 = new FriendRequest();
        request1.setFromUser(fromUser);
        request1.setToUser(toUser);
        request1.setStatus("PENDING");
        request1.setCreatedAt(Instant.now());
        request1.setUpdatedAt(Instant.now());

        FriendRequest request2 = new FriendRequest();
        request2.setFromUser(toUser);
        request2.setToUser(fromUser);
        request2.setStatus("PENDING");
        request2.setCreatedAt(Instant.now());
        request2.setUpdatedAt(Instant.now());

        friendRequestRepository.save(request1);
        friendRequestRepository.save(request2);

        Iterable<FriendRequest> requests = friendRequestRepository.findAll();
        assertThat(requests).hasSize(2);
    }

    @Test
    void updateStatusChangesApplied() {
        FriendRequest request = new FriendRequest();
        request.setFromUser(fromUser);
        request.setToUser(toUser);
        request.setStatus("PENDING");
        request.setCreatedAt(Instant.now());
        request.setUpdatedAt(Instant.now());

        FriendRequest savedRequest = friendRequestRepository.save(request);
        savedRequest.setStatus("ACCEPTED");
        savedRequest.setUpdatedAt(Instant.now());
        friendRequestRepository.save(savedRequest);

        Optional<FriendRequest> foundRequest = friendRequestRepository.findById(savedRequest.getId());
        assertThat(foundRequest).isPresent();
        assertThat(foundRequest.get().getStatus()).isEqualTo("ACCEPTED");
    }

    @Test
    void deleteRequestThenNotFound() {
        FriendRequest request = new FriendRequest();
        request.setFromUser(fromUser);
        request.setToUser(toUser);
        request.setStatus("PENDING");
        request.setCreatedAt(Instant.now());
        request.setUpdatedAt(Instant.now());

        FriendRequest savedRequest = friendRequestRepository.save(request);
        friendRequestRepository.deleteById(savedRequest.getId());

        Optional<FriendRequest> foundRequest = friendRequestRepository.findById(savedRequest.getId());
        assertThat(foundRequest).isEmpty();
    }
}