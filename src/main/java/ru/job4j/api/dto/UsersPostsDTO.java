package ru.job4j.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "User with their posts")
public class UsersPostsDTO {

    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Username of the user", example = "Mediator")
    private String userName;

    @Schema(description = "List of posts created by the user")
    private List<PostDTO> posts;
}
