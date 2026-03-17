package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.Job4jSocialMediaApiApplication;
import ru.job4j.api.model.Image;
import ru.job4j.api.model.Post;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.ImageRepository;
import ru.job4j.api.repository.PostRepository;
import ru.job4j.api.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Job4jSocialMediaApiApplication.class)
@ActiveProfiles("test")
@Transactional
class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Post testPost;

    @BeforeEach
    void setUp() {
        imageRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("imageuser");
        testUser.setEmail("image@example.com");
        testUser.setPasswordHash("hashed123");
        testUser = userRepository.save(testUser);

        testPost = new Post();
        testPost.setUser(testUser);
        testPost.setTitle("Test Post");
        testPost.setContent("Test Content");
        testPost.setCreatedAt(Instant.now());
        testPost.setUpdatedAt(Instant.now());
        testPost = postRepository.save(testPost);
    }

    @Test
    void saveImageThenFindById() {
        Image image = new Image();
        image.setPost(testPost);
        image.setFileName("test.jpg");
        image.setFilePath("/images/test.jpg");
        image.setFileSize(1024L);
        image.setContentType("image/jpeg");
        image.setCreatedAt(Instant.now());

        Image savedImage = imageRepository.save(image);
        Optional<Image> foundImage = imageRepository.findById(savedImage.getId());

        assertThat(foundImage).isPresent();
        assertThat(foundImage.get().getFileName()).isEqualTo("test.jpg");
        assertThat(foundImage.get().getFilePath()).isEqualTo("/images/test.jpg");
        assertThat(foundImage.get().getFileSize()).isEqualTo(1024L);
        assertThat(foundImage.get().getContentType()).isEqualTo("image/jpeg");
        assertThat(foundImage.get().getPost().getId()).isEqualTo(testPost.getId());
    }

    @Test
    void findAllReturnsAllImages() {
        Image image1 = new Image();
        image1.setPost(testPost);
        image1.setFileName("test1.jpg");
        image1.setFilePath("/images/test1.jpg");
        image1.setFileSize(1024L);
        image1.setContentType("image/jpeg");
        image1.setCreatedAt(Instant.now());

        Image image2 = new Image();
        image2.setPost(testPost);
        image2.setFileName("test2.jpg");
        image2.setFilePath("/images/test2.jpg");
        image2.setFileSize(2048L);
        image2.setContentType("image/jpeg");
        image2.setCreatedAt(Instant.now());

        imageRepository.save(image1);
        imageRepository.save(image2);

        Iterable<Image> images = imageRepository.findAll();
        assertThat(images).hasSize(2);
        assertThat(images).extracting(Image::getFileName)
                .containsExactlyInAnyOrder("test1.jpg", "test2.jpg");
    }

    @Test
    void updateImageChangesApplied() {
        Image image = new Image();
        image.setPost(testPost);
        image.setFileName("original.jpg");
        image.setFilePath("/images/original.jpg");
        image.setFileSize(1024L);
        image.setContentType("image/jpeg");
        image.setCreatedAt(Instant.now());

        Image savedImage = imageRepository.save(image);
        savedImage.setFileName("updated.jpg");
        savedImage.setFilePath("/images/updated.jpg");
        savedImage.setFileSize(2048L);
        imageRepository.save(savedImage);

        Optional<Image> foundImage = imageRepository.findById(savedImage.getId());
        assertThat(foundImage).isPresent();
        assertThat(foundImage.get().getFileName()).isEqualTo("updated.jpg");
        assertThat(foundImage.get().getFilePath()).isEqualTo("/images/updated.jpg");
        assertThat(foundImage.get().getFileSize()).isEqualTo(2048L);
    }

    @Test
    void deleteImageThenNotFound() {
        Image image = new Image();
        image.setPost(testPost);
        image.setFileName("test.jpg");
        image.setFilePath("/images/test.jpg");
        image.setFileSize(1024L);
        image.setContentType("image/jpeg");
        image.setCreatedAt(Instant.now());

        Image savedImage = imageRepository.save(image);
        imageRepository.deleteById(savedImage.getId());

        Optional<Image> foundImage = imageRepository.findById(savedImage.getId());
        assertThat(foundImage).isEmpty();
    }
}