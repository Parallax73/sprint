package com.sprint.ingest.controller;

import com.sprint.ingest.dto.RawLogRequest;
import com.sprint.ingest.exception.RateLimitExceededException;
import com.sprint.ingest.service.LogProducerService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j. Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework. http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java. util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/ingest")
@RequiredArgsConstructor
public class IngestController {

    private final LogProducerService logProducerService;

    private static final int MAX_PAYLOAD_SIZE = 10_000;
    private static final int RATE_LIMIT_PER_MINUTE = 100;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @PostMapping
    public ResponseEntity<Void> ingestLog(@Valid @RequestBody RawLogRequest request,
                                          HttpServletRequest httpRequest) {

        String clientIp = getClientIP(httpRequest);
        Bucket bucket = resolveBucket(clientIp);

        if (! bucket.tryConsume(1)) {
            throw new RateLimitExceededException("Rate limit exceeded for IP: " + clientIp);
        }

        if (request.rawPayload() != null && request.rawPayload().length() > MAX_PAYLOAD_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        }

        String sanitizedPayload = sanitizeInput(request.rawPayload());

        RawLogRequest normalized = new RawLogRequest(
                request.sourceIp(),
                request.destinationIp(),
                request. serviceName(),
                request.eventType(),
                sanitizedPayload,
                request.timestamp() != null ? request.timestamp() : Instant.now(),
                request. metadata()
        );

        logProducerService.sendLog(normalized);

        return ResponseEntity.accepted().build();
    }

    private Bucket resolveBucket(String ip) {
        return buckets. computeIfAbsent(ip, k -> {
            Bandwidth limit = Bandwidth.classic(RATE_LIMIT_PER_MINUTE,
                    Refill.intervally(RATE_LIMIT_PER_MINUTE, Duration.ofMinutes(1)));
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        });
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request. getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[<>\"']", "")
                .replaceAll("\\p{Cntrl}", "");
    }
}