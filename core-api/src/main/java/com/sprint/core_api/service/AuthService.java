package com.sprint.core_api.service;

import com.sprint.core_api.dto.request.CreateUserRequest;
import com.sprint.core_api.dto.request.LoginRequest;
import com. sprint.core_api.dto. response.AuthTokenResponse;
import com.sprint.core_api.dto.response.UserResponse;
import com.sprint.core_api. entity.User;
import com. sprint.core_api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j. Slf4j;
import org. springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public UserResponse register(CreateUserRequest request) {
        if (userRepository.findByUsername(request.username()). isPresent()) {
            throw new RuntimeException("Username already exists: " + request.username());
        }


        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists: " + request. email());
        }


        User user = new User();
        user.setUsername(request.username());
        user. setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setFullName(request.fullName());

        User savedUser = userRepository.save(user);

        log.info("New user registered: {}", savedUser.getUsername());

        return mapToUserResponse(savedUser);
    }


    @Transactional
    public AuthTokenResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );


        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));


        AuthTokenResponse tokens = jwtService. generateTokens(
                user.getUsername(),
                user.getRole(),
                httpRequest
        );

        log.info("User logged in: {}", user.getUsername());

        return tokens;
    }


    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
                );
    }
}