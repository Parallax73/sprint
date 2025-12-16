package com.sprint.analyzer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConfig {


    @Bean
    public DeadLetterPublishingRecoverer publisher(KafkaTemplate<String, Object> template) {
        return new DeadLetterPublishingRecoverer(template, (record, ex) -> {
            log.error("Message failed after retries. Moving to DLT. Topic: {}, Error: {}",
                    record.topic(), ex.getMessage());
            return new TopicPartition(record.topic() + ".DLT", record.partition());
        });
    }

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer recoverer) {
        // Retry 2 times with 1 second interval (3 attempts total)
        FixedBackOff backOff = new FixedBackOff(1000L, 2);
        return new DefaultErrorHandler(recoverer, backOff);
    }
}