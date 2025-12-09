package com.sprint.core_api.repository;

import com.sprint.core_api.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {


    Page<Alert> findAll(Pageable pageable);
}