package ru.job4j.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Model Information")
public class UserDTO {

    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "username не может быть пустым")
    @Length(min = 2,
    max = 20,
    message = "username должно быть не менее 2 и не более 10 символов")
    @Schema(description = "Username of the user", example = "Mediator", required = true)
    private String username;

    @Email
    @Schema(description = "Email address of the user", example = "mediator@example.com", required = true)
    private String email;

    @NotBlank(message = "пароль не может быть пустым")
    @Schema(description = "User password (hashed in database)",
            example = "********",
            accessMode = Schema.AccessMode.WRITE_ONLY)
    private String passwordHash;
}
