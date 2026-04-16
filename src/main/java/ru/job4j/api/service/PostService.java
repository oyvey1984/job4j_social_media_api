package ru.job4j.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.dto.UsersPostsDTO;
import ru.job4j.api.mappers.UserPostsDTOMapper;
import ru.job4j.api.model.Image;
import ru.job4j.api.model.Post;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.ImageRepository;
import ru.job4j.api.repository.PostRepository;
import ru.job4j.api.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final UserPostsDTOMapper userPostsDTOMapper;

    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository, UserPostsDTOMapper userPostsDTOMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.userPostsDTOMapper = userPostsDTOMapper;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public boolean update(Post post) {
        String content = post.getContent();
        String title = post.getTitle();
        Long id = post.getId();
        return postRepository.updateTitleAndContentByPostId(content, title, id) > 0L;
    }

    public boolean partialUpdate(Post post) {
        if (post.getId() == null) {
            return false;
        }

        Optional<Post> optionalPost = postRepository.findById(post.getId());
        if (optionalPost.isEmpty()) {
            return false;
        }

        Post existing = optionalPost.get();

        if (post.getTitle() != null) {
            existing.setTitle(post.getTitle());
        }

        if (post.getContent() != null) {
            existing.setContent(post.getContent());
        }

        postRepository.save(existing);
        return true;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public boolean deleteById(Long id) {
        return postRepository.deleteByPostId(id) > 0;
    }

    @Transactional
    public void createPost(Post post, List<Image> images, User currentUser) {
        post.setUser(currentUser);
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        Post savedPost = postRepository.save(post);

        images.forEach(image -> {
            image.setPost(savedPost);
            image.setCreatedAt(Instant.now());
        });
        imageRepository.saveAll(images);
    }

    @Transactional
    public void updatePost(Long postId, String newTitle, String newContent, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only edit your own posts");
        }

        int updated = postRepository.updateTitleAndContentByPostId(newTitle, newContent, postId);

        if (updated == 0) {
            throw new RuntimeException("Failed to update post");
        }
    }

    @Transactional
    public void deletePost(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own posts");
        }

        imageRepository.deleteImagesByPostId(postId);

        int deleted = postRepository.deleteByPostId(postId);

        if (deleted == 0) {
            throw new RuntimeException("Failed to delete post");
        }
    }

    public List<UsersPostsDTO> getUsersWithPosts(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }

        List<User> users = userRepository.findAllByIdIn(userIds);
        List<Post> allPosts = postRepository.findByUserIds(userIds);

        Map<Long, List<Post>> postsByUserId = allPosts.stream()
                .collect(Collectors.groupingBy(post -> post.getUser().getId()));

        return users.stream()
                .map(user -> userPostsDTOMapper.toDTO(user, postsByUserId.getOrDefault(user.getId(), List.of())
                ))
                .collect(Collectors.toList());
    }
}
