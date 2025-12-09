package com.sprint.core_api.repository;

import com.sprint.core_api.entity.DetectionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DetectionRuleRepository extends JpaRepository<DetectionRule, UUID> {

    Optional<DetectionRule> findByName(String name);

}
