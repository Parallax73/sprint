package com.sprint.agent.service;

import com.sprint.agent.service.parsers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogFileListenerFactory {

    private final IngestClient ingestClient;

    public LogFileListener createListener(String service, String type) {
        LogParser parser = createParser(type);
        return new LogFileListener(ingestClient, service, parser);
    }

    private LogParser createParser(String type) {
        return switch (type. toLowerCase()) {
            case "syslog" -> new SyslogParser();
            case "nginx" -> new NginxParser();
            case "apache" -> new ApacheParser();
            case "json" -> new JsonParser();
            default -> new PlainTextParser();
        };
    }
}