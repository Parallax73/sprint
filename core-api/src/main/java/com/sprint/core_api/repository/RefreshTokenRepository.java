package com.sprint.core_api.repository;

import com.sprint.core_api.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework. data.jpa.repository. Modifying;
import org.springframework.data.jpa.repository. Query;
import org.springframework. stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenId(String tokenId);

    List<RefreshToken> findByUsername(String username);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :revokedAt WHERE rt.username = :username")
    void revokeAllByUsername(String username, Instant revokedAt);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt. expiresAt < :now")
    void deleteExpiredTokens(Instant now);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :revokedAt WHERE rt.tokenId = :tokenId")
    void revokeByTokenId(String tokenId, Instant revokedAt);
}
