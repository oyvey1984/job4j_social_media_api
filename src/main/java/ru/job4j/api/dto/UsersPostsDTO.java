package ru.job4j.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsersPostsDTO {
    private Long userId;
    private String userName;
    private List<PostDTO> posts;
}
