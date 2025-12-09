package com.sprint.core_api.entity;


import com.sprint.core_api.dto.request.CreateLogRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "security_logs")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SecurityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String action;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;


    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String details;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public SecurityLog(CreateLogRequest request) {

        this.username = request.username();
        this.action = request.action();
        this.ipAddress = request.ipAddress();
        this.status = request.status();
        this.details = request.details();
        this.createdAt = Instant.now();

    }
}

