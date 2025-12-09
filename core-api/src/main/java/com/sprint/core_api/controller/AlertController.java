package com.sprint.core_api.controller;

import com.sprint.core_api.entity.Alert;
import com.sprint.core_api.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/core/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertRepository alertRepository;

    @GetMapping
    public ResponseEntity<Page<Alert>> getAlerts(
            @PageableDefault(size = 20, sort = "triggeredAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(alertRepository.findAll(pageable));
    }
}