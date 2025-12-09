package com.sprint.agent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngestClient {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Value("${sprint.ingest.url}")
    private String ingestUrl;

    @Value("${agent.name}")
    private String agentName;

    @Value("${agent.server.ip}")
    private String serverIp;

    public void sendLog(String line, String eventType) {
        try {
            // Construct the Payload matching RawLogRequest in Ingest Service
            Map<String, Object> payload = new HashMap<>();
            payload.put("sourceIp", serverIp);
            payload.put("serviceName", "sshd"); // Assuming we are watching SSH for now
            payload.put("eventType", eventType);
            payload.put("rawPayload", line);
            payload.put("metadata", Map.of("host", agentName));

            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ingestUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() != 202) {
                            log.error("Failed to send log. Status: {}", response.statusCode());
                        } else {
                            log.debug("Log sent successfully: {}", eventType);
                        }
                    });

        } catch (Exception e) {
            log.error("Error sending log: {}", e.getMessage());
        }
    }
}