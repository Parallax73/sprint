/**
 * Handles unauthorized access attempts in the JWT authentication flow.
 * Provides standardized JSON response for authentication failures.
 */
package com.sprint.core_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework. http.MediaType;
import org.springframework. security.core.AuthenticationException;
import org.springframework.security. web.AuthenticationEntryPoint;
import org.springframework.stereotype. Component;

import java.io. IOException;
import java.time. Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Entry point that gets triggered when unauthorized requests are made to secured endpoints.
 * Returns a structured JSON response with error details.
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handles unauthorized access by returning a JSON response with error details.
     * Sets 401 status code and includes timestamp, path and error message.
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        log.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> body = new HashMap<>();
        body. put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body. put("path", request.getServletPath());
        body.put("timestamp", Instant.now(). toString());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}