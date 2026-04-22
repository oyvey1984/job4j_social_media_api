package ru.job4j.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long postId;

    @NotNull(message = "userId не может быть null")
    private Long userId;

    @NotBlank(message = "username не может быть пустым")
    @Length(min = 2,
            max = 20,
            message = "username должно быть не менее 2 и не более 10 символов")
    private String userName;

    @NotBlank(message = "title не может быть пустым")
    @Length(min = 2,
            max = 20,
            message = "title должно быть не менее 2 и не более 100 символов")
    private String title;

    @NotBlank(message = "content не может быть пустым")
    @Length(min = 2,
            max = 20,
            message = "content должно быть не менее 100 и не более 10 000 символов")
    private String content;

    @NotBlank(message = "createdAt не может быть пустым")
    private String createdAt;
}
