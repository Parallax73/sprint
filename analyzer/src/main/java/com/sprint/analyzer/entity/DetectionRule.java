package com.sprint.analyzer.entity;

/**
 * Contains entity definitions for detection rules used to identify and classify security threats.
 * Rules are configured with conditions and severity levels to detect potential security incidents.
 */


import com.sprint.analyzer.enums.Severity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Represents a security detection rule that defines conditions and severity for identifying threats.
 * Each rule contains logic to evaluate security events and determine if they constitute an incident.
 */
@Entity
@Table(name = "detection_rules")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetectionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "condition_logic", nullable = false)
    private String conditionLogic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Severity severity;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;


}
