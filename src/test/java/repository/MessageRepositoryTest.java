package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.Job4jSocialMediaApiApplication;
import ru.job4j.api.model.Message;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.MessageRepository;
import ru.job4j.api.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Job4jSocialMediaApiApplication.class)
@ActiveProfiles("test")
@Transactional
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        messageRepository.deleteAll();
        userRepository.deleteAll();

        sender = new User();
        sender.setUsername("sender");
        sender.setEmail("sender@example.com");
        sender.setPasswordHash("hashed123");
        sender = userRepository.save(sender);

        receiver = new User();
        receiver.setUsername("receiver");
        receiver.setEmail("receiver@example.com");
        receiver.setPasswordHash("hashed456");
        receiver = userRepository.save(receiver);
    }

    @Test
    void saveMessageThenFindById() {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Hello, world!");
        message.setIsRead(false);
        message.setCreatedAt(Instant.now());

        Message savedMessage = messageRepository.save(message);
        Optional<Message> foundMessage = messageRepository.findById(savedMessage.getId());

        assertThat(foundMessage).isPresent();
        assertThat(foundMessage.get().getContent()).isEqualTo("Hello, world!");
        assertThat(foundMessage.get().getIsRead()).isFalse();
        assertThat(foundMessage.get().getSender().getId()).isEqualTo(sender.getId());
        assertThat(foundMessage.get().getReceiver().getId()).isEqualTo(receiver.getId());
    }

    @Test
    void findAllReturnsAllMessages() {
        Message msg1 = new Message();
        msg1.setSender(sender);
        msg1.setReceiver(receiver);
        msg1.setContent("Message 1");
        msg1.setIsRead(false);
        msg1.setCreatedAt(Instant.now());

        Message msg2 = new Message();
        msg2.setSender(receiver);
        msg2.setReceiver(sender);
        msg2.setContent("Message 2");
        msg2.setIsRead(false);
        msg2.setCreatedAt(Instant.now());

        messageRepository.save(msg1);
        messageRepository.save(msg2);

        Iterable<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(2);
        assertThat(messages).extracting(Message::getContent)
                .containsExactlyInAnyOrder("Message 1", "Message 2");
    }

    @Test
    void markAsReadThenIsReadTrue() {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Read me");
        message.setIsRead(false);
        message.setCreatedAt(Instant.now());

        Message savedMessage = messageRepository.save(message);
        savedMessage.setIsRead(true);
        messageRepository.save(savedMessage);

        Optional<Message> foundMessage = messageRepository.findById(savedMessage.getId());
        assertThat(foundMessage).isPresent();
        assertThat(foundMessage.get().getIsRead()).isTrue();
    }

    @Test
    void deleteMessageThenNotFound() {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("To be deleted");
        message.setIsRead(false);
        message.setCreatedAt(Instant.now());

        Message savedMessage = messageRepository.save(message);
        messageRepository.deleteById(savedMessage.getId());

        Optional<Message> foundMessage = messageRepository.findById(savedMessage.getId());
        assertThat(foundMessage).isEmpty();
    }
}