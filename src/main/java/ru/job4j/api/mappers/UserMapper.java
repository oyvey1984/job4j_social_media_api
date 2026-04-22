package ru.job4j.api.mappers;

import org.mapstruct.Mapper;
import ru.job4j.api.dto.UserDTO;
import ru.job4j.api.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
