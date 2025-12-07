package com.sprint.core_api.entity;

import com.sprint.core_api.DTO.CreateUserDTO;
import com.sprint.core_api.utils.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public User(CreateUserDTO createUserDTO) {
        this.username = createUserDTO.username();
        this.password = createUserDTO.password();
        this.role = UserRole.USER_ANALYST;
    }

}