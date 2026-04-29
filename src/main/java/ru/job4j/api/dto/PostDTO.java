package ru.job4j.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Post Model Information")
public class PostDTO {

    @Schema(description = "Unique identifier of the post", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long postId;

    @NotNull(message = "userId не может быть null")
    @Schema(description = "ID of the user who created the post", example = "5", required = true)
    private Long userId;

    @NotBlank(message = "username не может быть пустым")
    @Length(min = 2,
            max = 20,
            message = "username должно быть не менее 2 и не более 10 символов")
    @Schema(description = "Username of the author", example = "Mediator", required = true)
    private String userName;

    @NotBlank(message = "title не может быть пустым")
    @Length(min = 2,
            max = 20,
            message = "title должно быть не менее 2 и не более 100 символов")
    @Schema(description = "Post title", example = "Animals", required = true)
    private String title;

    @NotBlank(message = "content не может быть пустым")
    @Length(min = 2,
            max = 20,
            message = "content должно быть не менее 100 и не более 10 000 символов")
    @Schema(description = "Post content/body",
            example = "This is a detailed description of animals...",
            required = true)
    private String content;

    @NotBlank(message = "createdAt не может быть пустым")
    @Schema(description = "Creation date and time in ISO format", example = "2024-04-28T10:00:00Z", required = true)
    private String createdAt;
}
