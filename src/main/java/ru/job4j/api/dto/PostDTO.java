package ru.job4j.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDTO {
    private Long postId;
    private String userName;
    private String title;
    private String content;
    private String createdAt;
}
