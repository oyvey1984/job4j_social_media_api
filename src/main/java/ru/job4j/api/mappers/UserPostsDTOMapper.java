package ru.job4j.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.api.dto.UsersPostsDTO;
import ru.job4j.api.model.Post;
import ru.job4j.api.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserPostsDTOMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "posts", source = "posts")
    UsersPostsDTO toDTO(User user, List<Post> posts);
}
