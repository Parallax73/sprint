/**
 * User entity that manages user account data and authentication information.
 * Core domain entity for user management and authorization in the system.
 */
package com.sprint.core_api.entity;

import com.sprint.core_api.dto.request.CreateUserRequest;
import com.sprint.core_api.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a user account with authentication and profile information.
 * Manages user identity, credentials, and role-based access control.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Creates a new user with default USER_ANALYST role from registration request
     */
    public User(CreateUserRequest createUserRequest) {
        this.username = createUserRequest.username();
        this.password = createUserRequest.password();
        this.email = createUserRequest.email();
        this.fullName = createUserRequest.fullName();
        this.role = UserRole.USER_ANALYST;
    }

}