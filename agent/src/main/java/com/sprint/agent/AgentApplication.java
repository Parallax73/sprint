package com.sprint.agent;

import com.fasterxml.jackson.databind. ObjectMapper;
import com.sprint.agent.config.AgentConfiguration;
import com.sprint.agent.service.LogFileManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j. Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class AgentApplication {

    public static void main(String[] args) {
        SpringApplication. run(AgentApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public CommandLineRunner startAgent(LogFileManager logFileManager) {
        return args -> {
            log.info("🚀 Sprint Agent starting.. .");
            logFileManager.startWatching();
        };
    }
}