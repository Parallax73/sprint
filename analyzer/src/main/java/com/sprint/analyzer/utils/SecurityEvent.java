package com.sprint.analyzer.utils;

import java.util.Map;

public record SecurityEvent(
     String eventType,
     String username,
     String sourceIp,
     int riskScore,
     Map<String, String> metadata
){}
