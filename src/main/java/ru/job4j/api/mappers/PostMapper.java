package ru.job4j.api.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.api.dto.PostDTO;
import ru.job4j.api.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "postId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "createdAt", source = "createdAt")
    PostDTO toDTO(Post post);

    @InheritInverseConfiguration
    Post toEntity(PostDTO dto);
}
