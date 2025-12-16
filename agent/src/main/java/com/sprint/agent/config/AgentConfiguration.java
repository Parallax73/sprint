package com.sprint.agent.config;

import lombok.Data;
import org.springframework.boot.context. properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "agent")
@Data
public class AgentConfiguration {

    private String name;
    private String serverIp;
    private List<LogFileConfig> files = new ArrayList<>();

    @Data
    public static class LogFileConfig {
        private String path;
        private String service;
        private String type;
        private boolean enabled = true;
    }
}