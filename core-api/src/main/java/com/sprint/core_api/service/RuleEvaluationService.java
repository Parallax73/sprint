package com.sprint.core_api.service;


import com.sprint.core_api.entity.DetectionRule;
import com.sprint.core_api.repository.DetectionRuleRepository;
import com.sprint.core_api.utils.SecurityEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RuleEvaluationService {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final DetectionRuleRepository ruleRepository;

    public List<DetectionRule> evaluate(SecurityEvent event){

        log.info("Evaluating rules for event: {}", event);

        List<DetectionRule> activeRules = ruleRepository.findAllByIsActiveTrue();
        List<DetectionRule> matchedRules = new ArrayList<>();

        StandardEvaluationContext context = new StandardEvaluationContext(event);

        for (DetectionRule rule : activeRules) {
            try{
                Expression expression = parser.parseExpression(rule.getConditionLogic());

                Boolean isMatch = expression.getValue(context, Boolean.class);

                if (Boolean.TRUE.equals(isMatch)){
                    log.warn("Rule {} matched event: {}", rule.getName(), event);
                    matchedRules.add(rule);
                }
            } catch (ParseException | EvaluationException e) {
                log.error("Failed to evaluate rule '{}' (ID: {}) ", rule.getName(), rule.getId(), e);

            }
        }
        return matchedRules;
    }

}
