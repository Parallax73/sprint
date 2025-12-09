package com.sprint.agent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogFileListener extends TailerListenerAdapter {

    private final IngestClient ingestClient;

    @Override
    public void handle(String line) {
        log.info("Detected new log line: {}", line);

        // Basic parsing logic to determine Event Type
        // In a real agent, this might be more complex regex
        String eventType = "SYSTEM_LOG";

        if (line.contains("Failed password") || line.contains("authentication failure")) {
            eventType = "AUTH_FAILURE";
        } else if (line.contains("Accepted password")) {
            eventType = "AUTH_SUCCESS";
        }

        // Send to Ingest Service
        ingestClient.sendLog(line, eventType);
    }

    @Override
    public void handle(Exception ex) {
        log.error("Error reading log file: {}", ex.getMessage());
    }
}