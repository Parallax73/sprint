package com.sprint.core_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Entity representing a JWT refresh token used for authentication.
 * Tracks token state and associated metadata for security and audit purposes.
 */
@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    // JWT ID used for token tracking and revocation
    @Column(nullable = false, unique = true)
    private String tokenId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private Instant issuedAt;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = true)
    private Instant revokedAt;

    // Client metadata for security tracking
    private String ipAddress;
    private String userAgent;

    /**
     * Checks if the token has exceeded its expiration time
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    /**
     * Validates token is neither revoked nor expired
     */
    public boolean isValid() {
        return !revoked && !isExpired();
    }
}