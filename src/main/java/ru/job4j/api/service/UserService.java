package ru.job4j.api.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.api.dto.UserDTO;
import ru.job4j.api.mappers.UserMapper;
import ru.job4j.api.model.ERole;
import ru.job4j.api.model.Role;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.RoleRepository;
import ru.job4j.api.repository.UserRepository;
import ru.job4j.api.security.dtos.request.SignupRequestDTO;
import ru.job4j.api.security.dtos.response.RegisterDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private PasswordEncoder encoder;

    public RegisterDTO signUp(SignupRequestDTO signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))
                || Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            return new RegisterDTO(HttpStatus.BAD_REQUEST, "Error: Username or Email is already taken!");
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        Supplier<RuntimeException> supplier = () -> new RuntimeException("Error: Role is not found.");

        if (strRoles == null) {
            roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(supplier));
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(supplier));
                    case "mod" -> roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(supplier));
                    default -> roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(supplier));
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new RegisterDTO(HttpStatus.OK, "Person registered successfully!");
    }

    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public boolean update(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            return false;
        }

        User user = userMapper.toEntity(userDTO);
        return userRepository.update(user) > 0L;
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    public boolean deleteById(Long id) {
        return userRepository.delete(id) > 0;
    }

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean partialUpdate(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            return false;
        }

        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
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

        userRepository.save(existing);
        return true;
    }
}
