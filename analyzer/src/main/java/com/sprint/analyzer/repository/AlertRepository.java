package com.sprint.analyzer.repository;

import com.sprint.analyzer.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
}
