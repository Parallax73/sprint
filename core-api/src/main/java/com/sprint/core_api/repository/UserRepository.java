/**
 * Repository layer for managing User entity persistence operations.
 * Provides database access methods for user management functionality.
 */
package com.sprint.core_api.repository;

import com.sprint.core_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity that handles database operations.
 * Extends JPA functionality with custom queries for username and email lookups.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
