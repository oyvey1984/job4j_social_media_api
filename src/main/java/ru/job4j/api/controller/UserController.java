package ru.job4j.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.api.controller.api.UserControllerApi;
import ru.job4j.api.dto.UserDTO;
import ru.job4j.api.service.UserService;

import java.net.URI;
import java.util.List;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
public class UserController implements UserControllerApi {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserDTO> get(@PathVariable("userId")
                                    @NotNull
                                    @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                    Long userId) {
        return userService.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<UserDTO> save(@RequestBody @Valid UserDTO userDTO) {
        UserDTO savedUser = userService.save(userDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(savedUser);
    }

    @Override
    public ResponseEntity<Void> update(@RequestBody @Valid UserDTO userDTO) {
        if (userDTO.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (userService.update(userDTO)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> change(@RequestBody @Valid UserDTO userDTO) {
        if (userService.partialUpdate(userDTO)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> removeById(@PathVariable("userId")
                                           @NotNull
                                           @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                           Long userId) {
        if (userService.deleteById(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
