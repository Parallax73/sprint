package com.sprint.core_api.service;

import com.sprint.core_api.dto.request.CreateRuleRequest;
import com.sprint.core_api.dto.response.RuleResponse;
import com.sprint.core_api.entity.DetectionRule;
import com.sprint.core_api.enums.Severity;
import com.sprint.core_api.exception.ExistingResourceException;
import com.sprint.core_api.exception.NotFindResourceException;
import com.sprint.core_api.repository.DetectionRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleService {

    private final DetectionRuleRepository ruleRepository;

    /*
     * Create new Detection Rule
     * */
    @Transactional
    public RuleResponse createRule(CreateRuleRequest request){

        if (ruleRepository.findByName(request.name()).isPresent()){
            throw new ExistingResourceException("Rule already exists: " + request.name());
        }

        DetectionRule rule = new DetectionRule(request);
        ruleRepository.save(rule);

        log.info("New rule created: {}", request.name());

        return new RuleResponse(
                rule.getId(),
                rule.getName(),
                rule.getSeverity(),
                rule.isActive()
        );
    }

    /*
     * Delete a rule by ID
     * */
    @Transactional
    public void deleteRuleById(UUID id){

        if (ruleRepository.findById(id).isEmpty()){
            log.error("Error deleting rule: {}", id);
            throw new NotFindResourceException("Rule not found: " + id);
        }

        ruleRepository.deleteById(id);
        log.info("Rule deleted: {}", id);
    }

    /*
     Update rule's severity level
     * */
    @Transactional
    public RuleResponse updateRuleSeverity(UUID id, Severity severity){

        DetectionRule rule = ruleRepository.findById(id)
                .orElseThrow(()-> new NotFindResourceException("Rule not found: " + id));

        rule.setSeverity(severity);

        return new RuleResponse(rule.getId(), rule.getName(), rule.getSeverity(), rule.isActive());
    }

    /* * Update rules status
     * */
    @Transactional
    public RuleResponse updateRuleStatus(UUID id, boolean status){

        DetectionRule rule = ruleRepository.findById(id)
                .orElseThrow(()-> new NotFindResourceException("Rule not found: " + id));

        rule.setActive(status);

        return new RuleResponse(rule.getId(), rule.getName(), rule.getSeverity(), rule.isActive());
    }

    @Transactional
    public RuleResponse updateRuleCondition(UUID id, String conditionLogic){

        DetectionRule rule = ruleRepository.findById(id)
                .orElseThrow(()-> new NotFindResourceException("Rule not found: " + id));

        rule.setConditionLogic(conditionLogic);

        return new RuleResponse(rule.getId(), rule.getName(), rule.getSeverity(), rule.isActive());
    }

    @Transactional
    public RuleResponse updateRuleInfo(UUID id, String name, String description){

        DetectionRule rule = ruleRepository.findById(id)
                .orElseThrow(()-> new NotFindResourceException("Rule not found: " + id));

        // Check if name is changing and if it is unique
        if (name != null && !name.equals(rule.getName())) {
            if (ruleRepository.findByName(name).isPresent()){
                throw new ExistingResourceException("Rule already exists: " + name);
            }
            rule.setName(name);
        }

        if (description != null) {
            rule.setDescription(description);
        }

        return new RuleResponse(rule.getId(), rule.getName(), rule.getSeverity(), rule.isActive());
    }
}