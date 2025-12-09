package com.sprint.analyzer.entity;

import com.sprint.analyzer.enums.Severity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Column(nullable = false)
    private String username; // from the log

    @Column(name = "source_ip", nullable = false)
    private String sourceIp;

    @Column(name = "triggered_at", nullable = false)
    @CreationTimestamp
    private Instant triggeredAt;

    @Column(columnDefinition = "TEXT")
    private String details; // The raw log that caused it
}