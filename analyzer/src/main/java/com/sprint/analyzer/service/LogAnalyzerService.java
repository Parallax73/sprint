package com.sprint.analyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.analyzer.entity.Alert;
import com.sprint.analyzer.entity.DetectionRule;
import com.sprint.analyzer.repository.AlertRepository; // You need to create this simple repo
import com.sprint.analyzer.repository.DetectionRuleRepository;
import com.sprint.analyzer.utils.SecurityEvent; // The record you copied
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogAnalyzerService {

    private final DetectionRuleRepository ruleRepository;
    private final AlertRepository alertRepository;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ObjectMapper objectMapper = new ObjectMapper(); // To parse JSON logs

    @KafkaListener(topics = "${sprint.kafka.topic.raw-logs}", groupId = "sprint-analyzer-group")
    public void analyzeLog(Map<String, Object> rawLog) {
        log.info("Received log: {}", rawLog);

        // 1. Map Raw Log to Security Event (Normalization)
        // We assume the Ingest service sends a JSON structure matching our needs
        SecurityEvent event = new SecurityEvent(
                (String) rawLog.get("eventType"),
                "Unknown", // If username isn't parsed yet
                (String) rawLog.get("sourceIp"),
                0,
                (Map<String, String>) rawLog.get("metadata")
        );

        // 2. Fetch Active Rules
        List<DetectionRule> rules = ruleRepository.findAllByIsActiveTrue();

        // 3. Evaluate
        StandardEvaluationContext context = new StandardEvaluationContext(event);

        for (DetectionRule rule : rules) {
            try {
                Expression exp = parser.parseExpression(rule.getConditionLogic());
                Boolean match = exp.getValue(context, Boolean.class);

                if (Boolean.TRUE.equals(match)) {
                    triggerAlert(rule, event, rawLog.toString());
                }
            } catch (Exception e) {
                log.error("Rule evaluation failed for rule: {}", rule.getName(), e);
            }
        }
    }

    private void triggerAlert(DetectionRule rule, SecurityEvent event, String rawPayload) {
        log.warn("🚨 ALERT TRIGGERED: {} against IP {}", rule.getName(), event.sourceIp());

        Alert alert = Alert.builder()
                .ruleName(rule.getName())
                .severity(rule.getSeverity())
                .username(event.username())
                .sourceIp(event.sourceIp())
                .details(rawPayload)
                .build();

        alertRepository.save(alert);
        // Future: Produce to 'alerts' topic for Discord/Slack notification
    }
}