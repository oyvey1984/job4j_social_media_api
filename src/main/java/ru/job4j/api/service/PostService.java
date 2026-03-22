package ru.job4j.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.model.Image;
import ru.job4j.api.model.Post;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.ImageRepository;
import ru.job4j.api.repository.PostRepository;

import java.time.Instant;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    public PostService(PostRepository postRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
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
}
