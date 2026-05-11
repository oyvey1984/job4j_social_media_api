package ru.job4j.api.security.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.api.security.dtos.request.LoginRequestDTO;
import ru.job4j.api.security.dtos.request.SignupRequestDTO;
import ru.job4j.api.security.dtos.response.JwtResponseDTO;
import ru.job4j.api.security.dtos.response.MessageResponseDTO;
import ru.job4j.api.security.dtos.response.RegisterDTO;
import ru.job4j.api.security.jwt.JwtUtils;
import ru.job4j.api.security.userdetails.UserDetailsImpl;
import ru.job4j.api.service.UserService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDTO> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
        RegisterDTO registerDTO = userService.signUp(signUpRequest);
        return ResponseEntity.status(registerDTO.getStatus())
                .body(new MessageResponseDTO(registerDTO.getMessage()));
    }


    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return ResponseEntity
                .ok(new JwtResponseDTO(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles
                        ));
    }
}
