package com.sprint.core_api.repository;

import com.sprint.core_api.entity.SecurityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SecurityLogRepository extends JpaRepository<SecurityLog, UUID> {

    List<SecurityLog> findByUsernameOrderByCreatedAtDesc(String username);

    Page<SecurityLog> findByUsername(String username, Pageable pageable);
}