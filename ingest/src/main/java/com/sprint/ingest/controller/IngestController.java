package com.sprint.ingest.controller;

import com.sprint.ingest.dto.RawLogRequest;
import com.sprint.ingest.service.LogProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/ingest")
@RequiredArgsConstructor
public class IngestController {

    private final LogProducerService logProducerService;

    @PostMapping
    public ResponseEntity<Void> ingestLog(@Valid @RequestBody RawLogRequest request) {


        RawLogRequest normalized = new RawLogRequest(
                request.sourceIp(),
                request.destinationIp(),
                request.serviceName(),
                request.eventType(),
                request.rawPayload(),
                request.timestamp() != null ? request.timestamp() : Instant.now(),
                request.metadata()
        );

        logProducerService.sendLog(normalized);

        return ResponseEntity.accepted().build();
    }
}