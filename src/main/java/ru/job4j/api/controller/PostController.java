package ru.job4j.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.api.dto.PostDTO;
import ru.job4j.api.dto.UsersPostsDTO;
import ru.job4j.api.service.PostService;

import java.net.URI;
import java.util.List;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAll() {
        List<PostDTO> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> get(@PathVariable("postId")
                                    @NotNull
                                    @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                    Long postId) {
        return postService.findById(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostDTO> save(@RequestBody @Valid PostDTO postDTO) {
        PostDTO savedPost = postService.save(postDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getPostId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(savedPost);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid PostDTO postDTO) {
        if (postDTO.getPostId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (postService.update(postDTO)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping
    public ResponseEntity<Void> change(@RequestBody @Valid PostDTO postDTO) {
        if (postService.partialUpdate(postDTO)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> removeById(@PathVariable("postId")
                                           @NotNull
                                           @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                           Long postId) {
        if (postService.deleteById(postId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/users-with-posts")
    public ResponseEntity<List<UsersPostsDTO>> getUsersWithPosts(@RequestBody List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<UsersPostsDTO> result = postService.getUsersWithPosts(userIds);
        return ResponseEntity.ok(result);
    }
}

