package com.sprint.analyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.analyzer.entity.Alert;
import com.sprint.analyzer.entity.DetectionRule;
import com.sprint.analyzer.repository.AlertRepository;
import com.sprint.analyzer.repository.DetectionRuleRepository;
import com.sprint.analyzer.utils.SecurityEvent;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${sprint.kafka.topic.raw-logs}", groupId = "sprint-analyzer-group")
    public void analyzeLog(Map<String, Object> rawLog) {
        log.info("Received log: {}", rawLog);

        try {

            SecurityEvent event = mapToSecurityEvent(rawLog);
            List<DetectionRule> rules = ruleRepository.findAllByIsActiveTrue();
            evaluateRules(rules, event, rawLog);

        } catch (Exception e) {

            log.error("Critical processing failure. Sending to DLQ. Log: {}", rawLog, e);
            throw new RuntimeException("DLQ Trigger", e);
        }
    }


    @KafkaListener(topics = "${sprint.kafka.topic.raw-logs}.DLT", groupId = "sprint-analyzer-dlq-group")
    public void processDeadLetter(Map<String, Object> failedLog) {
        log.warn("💀 Received message in Dead Letter Topic: {}", failedLog);
        // TODO: Save to a 'failed_events' table or send Slack notification
    }

    private void evaluateRules(List<DetectionRule> rules, SecurityEvent event, Map<String, Object> rawLog) {
        StandardEvaluationContext context = new StandardEvaluationContext(event);

        for (DetectionRule rule : rules) {
            try {

                Expression exp = parser.parseExpression(rule.getConditionLogic());
                Boolean match = exp.getValue(context, Boolean.class);

                if (Boolean.TRUE.equals(match)) {
                    triggerAlert(rule, event, rawLog.toString());
                }
            } catch (Exception e) {

                log.error("Rule evaluation failed for Rule ID: {}", rule.getName(), e);
            }
        }
    }

    private SecurityEvent mapToSecurityEvent(Map<String, Object> rawLog) {
        return new SecurityEvent(
                (String) rawLog.getOrDefault("eventType", "UNKNOWN"),
                "Unknown",
                (String) rawLog.getOrDefault("sourceIp", "0.0.0.0"),
                0,
                (Map<String, String>) rawLog.get("metadata")
        );
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
    }
}