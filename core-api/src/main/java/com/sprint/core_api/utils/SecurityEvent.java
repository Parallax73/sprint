package com.sprint.core_api.utils;

import java.util.Map;

public record SecurityEvent (
     String eventType,
     String username,
     String sourceIp,
     int riskScore,
     Map<String, String> metadata
){}
