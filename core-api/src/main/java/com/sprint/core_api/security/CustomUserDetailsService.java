/**
 * Security service implementation that loads user details for authentication.
 * Provides user validation and role-based authority mapping for Spring Security.
 */
package com.sprint.core_api.security;

import com.sprint.core_api.repository.UserRepository;
import lombok. RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework. security.core.userdetails. UserDetailsService;
import org. springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service that bridges application users with Spring Security authentication.
 * Maps domain user entities to Spring Security UserDetails objects.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user details and maps authorities for authentication.
     * Throws UsernameNotFoundException if user doesn't exist.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(user. getUsername())
                .password(user.getPassword())
                . authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user. getRole(). name())
                ))
                . accountExpired(false)
                . accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}