/**
 * Authentication REST API controller that handles user registration, login, token management,
 * and session control for the core API.
 */
package com.sprint.core_api.controller;

import com.sprint.core_api.dto.request.CreateUserRequest;
import com.sprint.core_api.dto.request.LoginRequest;
import com.sprint.core_api.dto.request.RefreshTokenRequest;
import com.sprint.core_api.dto.response.AuthTokenResponse;
import com.sprint.core_api.dto.response.UserResponse;
import com.sprint.core_api.service.AuthService;
import com.sprint.core_api.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles authentication-related HTTP requests including user registration, login,
 * token refresh, and session management.
 */
@RestController
@RequestMapping("/api/v1/core")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody CreateUserRequest request) {

        log.info("Registration attempt for username: {}", request.username());

        UserResponse user = authService.register(request);

        log.info("User registered successfully: {}", user.username());

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        log.info("Login attempt for username: {}", request.username());

        AuthTokenResponse tokens = authService.login(request, httpRequest);

        ResponseCookie cookie = ResponseCookie.from("sprint-jwt", tokens.accessToken())
                .httpOnly(true)
                .secure(false) // Need to set to true in production, it needs HTTPS for true to work
                .path("/")
                .maxAge(24 * 60 * 60) // 1 day expiration
                .sameSite("Lax")
                .build();

        log.info("User logged in successfully: {}", request.username());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login successful");
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthTokenResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest) {

        log.info("Token refresh attempt");

        AuthTokenResponse tokens = jwtService.refreshAccessToken(
                request.refreshToken(),
                httpRequest
        );

        log.info("Token refreshed successfully");

        return ResponseEntity.ok(tokens);
    }

    /**
     * Handles user logout by invalidating the provided token.
     * For refresh tokens, revokes the specific token.
     * For access tokens, logs the logout attempt.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {

        log.info("Logout attempt");

        ResponseCookie cleanCookie = ResponseCookie.from("sprint-jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        try {
            if (authHeader != null) {
                String token = extractToken(authHeader);
                var decodedJWT = jwtService.verifyToken(token);
                String tokenType = decodedJWT.getClaim("token_type").asString();

                if ("refresh".equals(tokenType)) {
                    String tokenId = decodedJWT.getClaim("jti").asString();
                    jwtService.revokeToken(tokenId);
                } else {
                    String username = decodedJWT.getSubject();
                    log.info("Logging out user via access token: {}", username);
                }
            }

            log.info("User logged out successfully");

            return ResponseEntity.noContent()
                    .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                    .build();

        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                    .build();
        }
    }


    /**
     * Logs out user from all devices by revoking all active tokens for the user.
     */
    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(@RequestHeader("Authorization") String authHeader) {

        log.info("Logout all devices attempt");

        try {
            String token = extractToken(authHeader);
            var decodedJWT = jwtService.verifyToken(token);
            String username = decodedJWT.getSubject();

            jwtService.revokeAllUserTokens(username);

            log.info("User logged out from all devices: {}", username);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("Error during logout-all: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/validate")
    public ResponseEntity<@NonNull TokenValidationResponse> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = extractToken(authHeader);
            var decodedJWT = jwtService.verifyToken(token);

            String username = decodedJWT.getSubject();
            String tokenType = decodedJWT.getClaim("token_type").asString();

            TokenValidationResponse response = TokenValidationResponse.builder()
                    .valid(true)
                    .username(username)
                    .tokenType(tokenType)
                    .expiresAt(decodedJWT.getExpiresAt().toInstant())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());

            TokenValidationResponse response = TokenValidationResponse.builder()
                    .valid(false)
                    .message(e.getMessage())
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    /**
     * Extracts JWT token from Authorization header by removing "Bearer " prefix.
     * Throws IllegalArgumentException if header is invalid.
     */
    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authHeader.substring(7);
    }


    @Data
    @Builder
    public static class TokenValidationResponse {
        private boolean valid;
        private String username;

        private String tokenType;
        private java.time.Instant expiresAt;
        private String message;
    }
}