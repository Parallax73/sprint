package com.sprint.core_api.controller;

import com.sprint.core_api.dto.request.CreateRuleRequest;
import com.sprint.core_api.dto.response.RuleResponse;
import com.sprint.core_api.entity.DetectionRule;
import com.sprint.core_api.enums.Severity;
import com.sprint.core_api.repository.DetectionRuleRepository;
import com.sprint.core_api.service.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/core/rules")
@RequiredArgsConstructor
public class DetectionRuleController {

    private final RuleService ruleService;
    private final DetectionRuleRepository ruleRepository;

    @PostMapping
    public ResponseEntity<RuleResponse> createRule(@Valid @RequestBody CreateRuleRequest request) {
        return ResponseEntity.ok(ruleService.createRule(request));
    }

    @GetMapping
    public ResponseEntity<List<DetectionRule>> getAllRules() {
        return ResponseEntity.ok(ruleRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        ruleService.deleteRuleById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/severity")
    public ResponseEntity<RuleResponse> updateSeverity(
            @PathVariable UUID id,
            @RequestParam Severity severity) {
        return ResponseEntity.ok(ruleService.updateRuleSeverity(id, severity));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RuleResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(ruleService.updateRuleStatus(id, active));
    }
}