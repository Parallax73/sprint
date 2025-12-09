package com.sprint.ingest.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.Map;

public record RawLogRequest(

        @NotBlank(message = "Source IP is required")
        String sourceIp,

        String destinationIp,

        @NotBlank(message = "Service name is required")
        String serviceName,

        @NotBlank(message = "Event type is required")
        String eventType,

        @NotBlank(message = "Raw payload is required")
        String rawPayload,

        Instant timestamp,

        Map<String, String> metadata
) {}