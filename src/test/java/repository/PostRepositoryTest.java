package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.Job4jSocialMediaApiApplication;
import ru.job4j.api.model.Post;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.PostRepository;
import ru.job4j.api.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Job4jSocialMediaApiApplication.class)
@ActiveProfiles("test")
@Transactional
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("postuser");
        testUser.setEmail("post@example.com");
        testUser.setPasswordHash("hashed123");
        testUser = userRepository.save(testUser);
    }

    @Test
    void whenSavePostThenFindById() {
        Post post = new Post();
        post.setUser(testUser);
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        Post savedPost = postRepository.save(post);
        Optional<Post> foundPost = postRepository.findById(savedPost.getId());

        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getTitle()).isEqualTo("Test Post");
        assertThat(foundPost.get().getContent()).isEqualTo("Test Content");
        assertThat(foundPost.get().getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void whenFindAllThenReturnAllPosts() {
        Post post1 = new Post();
        post1.setUser(testUser);
        post1.setTitle("Post 1");
        post1.setContent("Content 1");
        post1.setCreatedAt(Instant.now());
        post1.setUpdatedAt(Instant.now());

        Post post2 = new Post();
        post2.setUser(testUser);
        post2.setTitle("Post 2");
        post2.setContent("Content 2");
        post2.setCreatedAt(Instant.now());
        post2.setUpdatedAt(Instant.now());

        postRepository.save(post1);
        postRepository.save(post2);

        Iterable<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getTitle)
                .containsExactlyInAnyOrder("Post 1", "Post 2");
    }

    @Test
    void whenUpdatePostThenChangesApplied() {
        Post post = new Post();
        post.setUser(testUser);
        post.setTitle("Original Title");
        post.setContent("Original Content");
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        Post savedPost = postRepository.save(post);
        savedPost.setTitle("Updated Title");
        savedPost.setContent("Updated Content");
        savedPost.setUpdatedAt(Instant.now());
        postRepository.save(savedPost);

        Optional<Post> foundPost = postRepository.findById(savedPost.getId());
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getTitle()).isEqualTo("Updated Title");
        assertThat(foundPost.get().getContent()).isEqualTo("Updated Content");
    }

    @Test
    void whenDeletePostThenNotFound() {
        Post post = new Post();
        post.setUser(testUser);
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        Post savedPost = postRepository.save(post);
        postRepository.deleteById(savedPost.getId());

        Optional<Post> foundPost = postRepository.findById(savedPost.getId());
        assertThat(foundPost).isEmpty();
    }
}