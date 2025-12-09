package com.sprint.analyzer.repository;

import com.sprint.analyzer.entity.DetectionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DetectionRuleRepository extends JpaRepository<DetectionRule, UUID> {

    Optional<DetectionRule> findByName(String name);

    List<DetectionRule> findAllByIsActiveTrue();
}
