/**
 * JWT-based authentication service that manages access and refresh tokens.
 * Handles token generation, verification, and lifecycle management for secure user authentication.
 */
package com.sprint.core_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sprint.core_api.dto.response.AuthTokenResponse;
import com.sprint.core_api.entity.RefreshToken;
import com.sprint.core_api.entity.User;
import com.sprint.core_api.repository.RefreshTokenRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sprint.core_api.enums.UserRole;
import com.sprint.core_api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

/**
 * Service responsible for JWT token operations including generation, validation,
 * and refresh token management with security measures against token reuse.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer:Sprint Authentication}")
    private String issuer;

    private static final long ACCESS_TOKEN_VALIDITY_MINUTES = 15;
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 7;

    private static final int MAX_REFRESH_TOKENS_PER_USER = 5;

    /**
     * Generates new access and refresh token pair for a user.
     * Creates refresh token record and manages token count per user.
     */
    @Transactional
    public AuthTokenResponse generateTokens(String username, UserRole role, HttpServletRequest request) {
        Instant now = Instant.now();

        String accessToken = createAccessToken(username, role, now);

        String tokenId = UUID.randomUUID().toString();
        String refreshToken = createRefreshToken(username, tokenId, now);

        // Store refresh token in database
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setTokenId(tokenId);
        refreshTokenEntity.setUsername(username);
        refreshTokenEntity.setIssuedAt(now);
        refreshTokenEntity.setExpiresAt(now.plus(REFRESH_TOKEN_VALIDITY_DAYS, ChronoUnit.DAYS));
        refreshTokenEntity.setIpAddress(getClientIP(request));
        refreshTokenEntity.setUserAgent(request.getHeader("User-Agent"));

        refreshTokenRepository. save(refreshTokenEntity);

        cleanupOldTokens(username);

        log.info("Generated tokens for user: {}", username);

        return new AuthTokenResponse(accessToken, refreshToken, "Bearer");
    }


    /**
     * Validates refresh token and issues new access/refresh token pair.
     * Implements refresh token rotation for security.
     */
    @Transactional
    public AuthTokenResponse refreshAccessToken(String refreshTokenStr, HttpServletRequest request) {
        try {

            DecodedJWT decodedJWT = verifyToken(refreshTokenStr);
            String tokenId = decodedJWT. getClaim("jti").asString();
            String username = decodedJWT. getSubject();


            RefreshToken refreshToken = refreshTokenRepository.findByTokenId(tokenId)
                    .orElseThrow(() -> new RuntimeException("Refresh token not found"));

            if (!refreshToken.isValid()) {
                throw new RuntimeException("Refresh token is invalid or revoked");
            }


            if (!refreshToken.getUsername().equals(username)) {
                log.warn("Token username mismatch.  Expected: {}, Got: {}", refreshToken.getUsername(), username);
                throw new RuntimeException("Invalid token");
            }


            UserRole role = getUserRole(username);

            String newAccessToken = createAccessToken(username, role, Instant.now());


            String newRefreshToken = rotateRefreshToken(refreshToken, request);

            log.info("Refreshed access token for user: {}", username);

            return new AuthTokenResponse(newAccessToken, newRefreshToken, "Bearer");

        } catch (JWTVerificationException e) {
            log.error("Invalid refresh token: {}", e.getMessage());
            throw new RuntimeException("Invalid refresh token", e);
        }
    }


    /**
     * Rotates refresh token by invalidating the old one and generating a new one.
     * Security measure to prevent refresh token reuse.
     */
    @Transactional
    protected String rotateRefreshToken(RefreshToken oldToken, HttpServletRequest request) {

        oldToken. setRevoked(true);
        oldToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(oldToken);

        Instant now = Instant.now();
        String newTokenId = UUID.randomUUID(). toString();
        String newRefreshTokenStr = createRefreshToken(oldToken.getUsername(), newTokenId, now);


        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(newRefreshTokenStr);
        newRefreshToken.setTokenId(newTokenId);
        newRefreshToken.setUsername(oldToken.getUsername());
        newRefreshToken.setIssuedAt(now);
        newRefreshToken.setExpiresAt(now.plus(REFRESH_TOKEN_VALIDITY_DAYS, ChronoUnit.DAYS));
        newRefreshToken. setIpAddress(getClientIP(request));
        newRefreshToken.setUserAgent(request. getHeader("User-Agent"));

        refreshTokenRepository.save(newRefreshToken);

        return newRefreshTokenStr;
    }


    private String createAccessToken(String username, UserRole role, Instant issuedAt) {
        return JWT.create()
                .withSubject(username)
                . withClaim("role", role.toString())
                .withClaim("token_type", "access")
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(issuedAt.plus(ACCESS_TOKEN_VALIDITY_MINUTES, ChronoUnit.MINUTES)))
                .withIssuer(issuer)
                .sign(Algorithm. HMAC256(secret));
    }


    private String createRefreshToken(String username, String tokenId, Instant issuedAt) {
        return JWT.create()
                .withSubject(username)
                .withClaim("jti", tokenId)
                .withClaim("token_type", "refresh")
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(issuedAt.plus(REFRESH_TOKEN_VALIDITY_DAYS, ChronoUnit. DAYS)))
                .withIssuer(issuer)
                . sign(Algorithm.HMAC256(secret));
    }


    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build();
        return verifier.verify(token);
    }


    @Transactional
    public void revokeAllUserTokens(String username) {
        refreshTokenRepository.revokeAllByUsername(username, Instant.now());
        log.info("Revoked all tokens for user: {}", username);
    }


    @Transactional
    public void revokeToken(String tokenId) {
        refreshTokenRepository.revokeByTokenId(tokenId, Instant. now());
        log.info("Revoked token: {}", tokenId);
    }


    private void cleanupOldTokens(String username) {
        var tokens = refreshTokenRepository.findByUsername(username);
        if (tokens.size() >= MAX_REFRESH_TOKENS_PER_USER) {
            tokens.stream()
                    .sorted(Comparator.comparing(RefreshToken::getIssuedAt))
                    .limit(tokens.size() - MAX_REFRESH_TOKENS_PER_USER + 1)
                    .forEach(token -> {
                        token.setRevoked(true);
                        token.setRevokedAt(Instant.now());
                        refreshTokenRepository.save(token);
                    });
        }
    }


    /**
     * Scheduled job to remove expired refresh tokens from database.
     * Runs daily at 2 AM.
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(Instant.now());
        log.info("Cleaned up expired refresh tokens");
    }


    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }


    private UserRole getUserRole(String username) {
        return userRepository.findByUsername(username)
                .map(User::getRole)
                .orElseThrow(() -> new RuntimeException("User not found:  " + username));
    }
}