package ru.job4j.api.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.api.dto.PostDTO;
import ru.job4j.api.dto.UsersPostsDTO;

import java.util.List;

@Tag(name = "PostController", description = "PostController management APIs")
@RequestMapping("api/posts")
public interface PostControllerApi {

    @Operation(
            summary = "Retrieve all posts",
            description = "Returns a list of all posts. The response includes postId, userId, userName, title, content and creation date.",
            tags = { "PostController" }
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of posts retrieved successfully",
            content = { @Content(schema = @Schema(implementation = PostDTO.class), mediaType = "application/json") }
    )
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAll();

    @Operation(
            summary = "Retrieve a post by postId",
            description = "Get a post object by specifying its postId. The response includes postId, userId, userName, title, content and creation date.",
            tags = { "PostController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post found successfully",
                    content = { @Content(schema = @Schema(implementation = PostDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid postId format (must be >= 1)",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Post with specified ID not found",
                    content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> get(@PathVariable("postId")
                                       @NotNull
                                       @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                       Long postId);

    @Operation(
            summary = "Create a new post",
            description = "Creates a new post with the provided data. Requires userId, userName, title, content and createdAt. Returns the created post with generated postId.",
            tags = { "PostController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post created successfully",
                    content = { @Content(schema = @Schema(implementation = PostDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid input data (validation failed)",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User with specified userId not found",
                    content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<PostDTO> save(@RequestBody @Valid PostDTO postDTO);

    @Operation(
            summary = "Update an existing post",
            description = "Fully updates an existing post. Requires all fields to be provided. The postId must exist.",
            tags = { "PostController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post updated successfully (no content returned)",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data or missing postId",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Post with specified postId not found",
                    content = { @Content(schema = @Schema()) })
    })
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid PostDTO postDTO);

    @Operation(
            summary = "Partially update a post",
            description = "Updates only the provided fields of an existing post. PostId is required.",
            tags = { "PostController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post partially updated successfully",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Post with specified postId not found",
                    content = { @Content(schema = @Schema()) })
    })
    @PatchMapping
    public ResponseEntity<Void> change(@RequestBody @Valid PostDTO postDTO);

    @Operation(
            summary = "Delete a post by postId",
            description = "Deletes an existing post. The postId must be valid and exist in the database. All associated images will also be deleted.",
            tags = { "PostController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post deleted successfully (no content returned)",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", description = "Invalid postId format (must be >= 1)",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Post with specified postId not found",
                    content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> removeById(@PathVariable("postId")
                                           @NotNull
                                           @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                           Long postId);

    @Operation(
            summary = "Get users with their posts",
            description = "Takes a list of userIds and returns each user with their list of posts. Useful for batch fetching user data with posts.",
            tags = { "PostController" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users with posts retrieved successfully",
                    content = { @Content(schema = @Schema(implementation = UsersPostsDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Empty or null userIds list provided",
                    content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/users-with-posts")
    public ResponseEntity<List<UsersPostsDTO>> getUsersWithPosts(@RequestBody List<Long> userIds);
}
