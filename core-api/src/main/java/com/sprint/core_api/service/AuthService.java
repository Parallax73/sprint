/**
 * Authentication service that handles user registration and login functionality.
 * Manages JWT token generation and user credential validation.
 */
package com.sprint.core_api.service;

import com.sprint.core_api.dto.request.CreateUserRequest;
import com.sprint.core_api.dto.request.LoginRequest;
import com.sprint.core_api.dto.response.AuthTokenResponse;
import com.sprint.core_api.dto.response.UserResponse;
import com.sprint.core_api.entity.DetectionRule;
import com.sprint.core_api.entity.User;
import com.sprint.core_api.enums.Severity;
import com.sprint.core_api.exception.ExistingResourceException;
import com.sprint.core_api.repository.UserRepository;
import com.sprint.core_api.utils.SecurityEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for user authentication operations including registration and login.
 * Handles password encryption, JWT token generation, and user validation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RuleEvaluationService ruleEvaluationService;
    private final SecurityLogService securityLogService;


    /**
     * Registers a new user after validating username and email uniqueness.
     * Encrypts password and persists user details in the database.
     */
    @Transactional
    public UserResponse register(CreateUserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new ExistingResourceException("Username already exists: " + request.username());
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ExistingResourceException("Email already exists: " + request.email());
        }


        User user = new User(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);

        log.info("New user registered: {}", savedUser.getUsername());

        return mapToUserResponse(savedUser);
    }


    /**
     * Authenticates user credentials and generates JWT tokens upon successful login.
     * Uses Spring Security's authentication manager for validation.
     */
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

        Map<String, String> metadata = new HashMap<>();
        metadata.put("User-Agent", httpRequest.getHeader("User-Agent"));
        metadata.put("Time", java.time.Instant.now().toString());

        SecurityEvent event = new SecurityEvent(
                "LOGIN_ATTEMPT",
                user.getUsername(),
                getClientIP(httpRequest),
                0,
                metadata
        );

        List<DetectionRule> triggeredRules = ruleEvaluationService.evaluate(event);

        for (DetectionRule rule : triggeredRules) {
            log.warn("Security Alert: Rule '{}' triggered for user '{}'", rule.getName(), user.getUsername());

            if (rule.getSeverity() == Severity.CRITICAL) {

                securityLogService.saveLog(
                        user.getUsername(),
                        "LOGIN_ATTEMPT",
                        getClientIP(httpRequest),
                        "BLOCKED",
                        "Blocked by rule: " + rule.getName()
                );

                throw new RuntimeException("Login blocked by security rule: " + rule.getName());
            }
        }

        AuthTokenResponse tokens = jwtService.generateTokens(
                user.getUsername(),
                user.getRole(),
                httpRequest
        );

        log.info("User logged in: {}", user.getUsername());

        securityLogService.saveLog(
                user.getUsername(),
                "LOGIN_ATTEMPT",
                getClientIP(httpRequest),
                "SUCCESS",
                "Login successful"
        );

        return tokens;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
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