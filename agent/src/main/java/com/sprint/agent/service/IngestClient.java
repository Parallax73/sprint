package com.sprint.agent.service;

import com.fasterxml.jackson.databind. ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation. Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent. atomic.AtomicLong;

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

    private final AtomicLong sentCount = new AtomicLong(0);
    private final AtomicLong failedCount = new AtomicLong(0);

    public void sendLog(String line, String eventType, String serviceName, Map<String, String> parsedData) {
        CompletableFuture. runAsync(() -> {
            try {
                Map<String, Object> payload = new HashMap<>();
                payload. put("sourceIp", serverIp);
                payload.put("serviceName", serviceName);
                payload.put("eventType", eventType);
                payload.put("rawPayload", line);

                Map<String, String> metadata = new HashMap<>(parsedData);
                metadata.put("host", agentName);
                metadata.put("agentVersion", "1.0.0");

                payload.put("metadata", metadata);

                String json = objectMapper.writeValueAsString(payload);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(ingestUrl))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(10))
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        . build();

                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> {
                            if (response.statusCode() == 202) {
                                sentCount.incrementAndGet();
                                log.debug("✅ Log sent:  {}", eventType);
                            } else {
                                failedCount.incrementAndGet();
                                log. error("❌ Failed to send log.  Status: {}", response.statusCode());
                            }
                        })
                        .exceptionally(ex -> {
                            failedCount.incrementAndGet();
                            log.error("❌ Network error sending log", ex);
                            return null;
                        });

            } catch (Exception e) {
                failedCount.incrementAndGet();
                log.error("❌ Error preparing log", e);
            }
        });
    }

    @Scheduled(fixedRate = 60000)
    public void logStats() {
        long sent = sentCount.getAndSet(0);
        long failed = failedCount.getAndSet(0);

        if (sent > 0 || failed > 0) {
            log.info("📊 Stats: {} sent, {} failed (last minute)", sent, failed);
        }
    }
}