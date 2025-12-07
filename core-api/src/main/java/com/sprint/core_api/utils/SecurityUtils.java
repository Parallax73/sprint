package com.sprint.core_api.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {

    private SecurityUtils() {
        // Utility class
    }

    /**
     * Get the current authenticated username
     */
    public static Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder. getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return Optional. of(((UserDetails) principal). getUsername());
        } else if (principal instanceof String) {
            return Optional.of((String) principal);
        }

        return Optional.empty();
    }

    /**
     * Get the current UserDetails
     */
    public static Optional<UserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || ! authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication. getPrincipal();

        if (principal instanceof UserDetails) {
            return Optional.of((UserDetails) principal);
        }

        return Optional.empty();
    }

    /**
     * Check if the current user has a specific role
     */
    public static boolean hasRole(String role) {
        return getCurrentUser()
                .map(user -> user.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role)))
                .orElse(false);
    }
}