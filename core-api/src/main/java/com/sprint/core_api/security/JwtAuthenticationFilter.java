/**
 * JWT authentication filter implementation that validates and processes JWT tokens.
 * Handles token extraction, validation, and user authentication for protected endpoints.
 */
package com.sprint.core_api.security;

import com.sprint.core_api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http. HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j. Slf4j;
import org. springframework.lang.NonNull;
import org. springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org. springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter that intercepts requests to validate JWT tokens and authenticate users.
 * Skips validation for public endpoints defined in PUBLIC_PATHS.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private static final List<String> PUBLIC_PATHS = List. of(
            "/api/v1/core/register",
            "/api/v1/core/login",
            "/api/v1/core/refresh",
            "/api/v1/core/validate",
            "/health",
            "/actuator"
    );

    /**
     * Processes each request to validate JWT token and authenticate user if token is valid.
     * Skips validation for public paths and handles token extraction/validation errors.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String requestPath = request.getServletPath();

        log.debug("Processing request: {} {}", request.getMethod(), requestPath);


        if (isPublicPath(requestPath)) {
            log.debug("Skipping JWT filter for public path: {}", requestPath);
            filterChain. doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Bearer token found, continuing filter chain");
            filterChain. doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);

            var decodedJWT = jwtService.verifyToken(jwt);
            final String username = decodedJWT.getSubject();
            final String tokenType = decodedJWT.getClaim("token_type").asString();


            if (!"access".equals(tokenType)) {
                log.warn("Invalid token type used for authentication: {}", tokenType);
                filterChain.doFilter(request, response);
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("Authenticated user: {}", username);
            }

        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain. doFilter(request, response);
    }


    /**
     * Checks if the given path matches any of the defined public endpoints
     * that don't require authentication.
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}