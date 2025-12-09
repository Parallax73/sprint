/**
 * JWT configuration properties used for token generation and validation.
 * Maps properties from application.properties/yml with 'jwt' prefix.
 */
package com.sprint.core_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that holds JWT-related settings like secret key,
 * token validity periods and issuer information.
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    /**
     * Secret key used for signing JWT tokens
     */
    private String secret;
    /**
     * JWT issuer identifier
     */
    private String issuer = "Sprint Authentication";
    /**
     * Access token validity period in minutes
     */
    private long accessTokenValidityMinutes = 15;
    /**
     * Refresh token validity period in days
     */
    private long refreshTokenValidityDays = 7;
}
