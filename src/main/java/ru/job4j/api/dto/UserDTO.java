package ru.job4j.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank(message = "username не может быть пустым")
    @Length(min = 2,
    max = 20,
    message = "username должно быть не менее 2 и не более 10 символов")
    private String username;

    @Email
    private String email;

    @NotBlank(message = "пароль не может быть пустым")
    private String passwordHash;
}
