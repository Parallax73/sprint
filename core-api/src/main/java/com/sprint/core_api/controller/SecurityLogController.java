package com.sprint.core_api.controller;

import com.sprint.core_api.entity.SecurityLog;
import com.sprint.core_api.repository.SecurityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/core/security-logs")
@RequiredArgsConstructor
public class SecurityLogController {

    private final SecurityLogRepository securityLogRepository;

    @GetMapping
    public ResponseEntity<Page<SecurityLog>> getAllLogs(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(securityLogRepository.findAll(pageable));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Page<SecurityLog>> getLogsByUser(
            @PathVariable String username,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(securityLogRepository.findByUsername(username, pageable));
    }
}