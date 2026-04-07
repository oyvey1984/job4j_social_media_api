package ru.job4j.api.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public boolean update(User user) {
        return repository.update(user) > 0L;
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public boolean deleteById(Long id) {
        return repository.delete(id) > 0;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public boolean partialUpdate(User user) {
        if (user.getId() == null) {
            return false;
        }

        Optional<User> optionalUser = repository.findById(user.getId());
        if (optionalUser.isEmpty()) {
            return false;
        }

        User existing = optionalUser.get();

        if (user.getUsername() != null) {
            existing.setUsername(user.getUsername());
        }

        if (user.getEmail() != null) {
            existing.setEmail(user.getEmail());
        }

        if (user.getPasswordHash() != null) {
            existing.setPasswordHash(user.getPasswordHash());
        }

        repository.save(existing);
        return true;
    }
}
