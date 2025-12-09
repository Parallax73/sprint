package com.sprint.ingest.service;

import com.sprint.ingest.dto.RawLogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogProducerService {

    private final KafkaTemplate<String, RawLogRequest> kafkaTemplate;

    @Value("${sprint.kafka.topic.raw-logs}")
    private String topicName;

    public void sendLog(RawLogRequest logRequest) {
        log.debug("Pushing log to Kafka: {} - {}", logRequest.serviceName(), logRequest.eventType());

        // Use sourceIp as the key to ensure logs from the same IP land in the same partition
        // This guarantees strict ordering for a single attacker
        kafkaTemplate.send(topicName, logRequest.sourceIp(), logRequest);
    }
}