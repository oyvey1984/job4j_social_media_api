package ru.job4j.api.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.api.dto.UserDTO;
import ru.job4j.api.mappers.UserMapper;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = repository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public boolean update(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            return false;
        }

        User user = userMapper.toEntity(userDTO);
        return repository.update(user) > 0L;
    }

    public Optional<UserDTO> findById(Long id) {
        return repository.findById(id)
                .map(userMapper::toDTO);
    }

    public boolean deleteById(Long id) {
        return repository.delete(id) > 0;
    }

    public List<UserDTO> findAll() {
        List<User> users = repository.findAll();
        return users.stream().map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean partialUpdate(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            return false;
        }

        Optional<User> optionalUser = repository.findById(userDTO.getId());
        if (optionalUser.isEmpty()) {
            return false;
        }

        User existing = optionalUser.get();

        if (userDTO.getUsername() != null) {
            existing.setUsername(userDTO.getUsername());
        }

        if (userDTO.getEmail() != null) {
            existing.setEmail(userDTO.getEmail());
        }

        repository.save(existing);
        return true;
    }
}
