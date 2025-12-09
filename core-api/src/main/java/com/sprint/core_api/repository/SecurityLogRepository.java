package com.sprint.core_api.repository;

import com.sprint.core_api.entity.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SecurityLogRepository extends JpaRepository<SecurityLog, UUID> {

    List<SecurityLog> findByUsernameOrderByCreatedAtDesc(String username);

}