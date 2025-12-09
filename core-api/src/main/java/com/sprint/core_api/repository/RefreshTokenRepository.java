/**
 * Repository for managing refresh tokens in the authentication system.
 * Handles token persistence, revocation, and cleanup of expired tokens.
 */
package com.sprint.core_api.repository;

import com.sprint.core_api.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework. data.jpa.repository. Modifying;
import org.springframework.data.jpa.repository. Query;
import org.springframework. stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CRUD operations and custom queries on RefreshToken entities.
 * Provides methods for token management and revocation.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenId(String tokenId);

    List<RefreshToken> findByUsername(String username);

    /**
     * Revokes all refresh tokens for a specific user by marking them as revoked
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :revokedAt WHERE rt.username = :username")
    void revokeAllByUsername(String username, Instant revokedAt);

    /**
     * Removes expired tokens from the database as part of cleanup
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt. expiresAt < :now")
    void deleteExpiredTokens(Instant now);

    /**
     * Revokes a specific refresh token by its ID
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :revokedAt WHERE rt.tokenId = :tokenId")
    void revokeByTokenId(String tokenId, Instant revokedAt);
}
