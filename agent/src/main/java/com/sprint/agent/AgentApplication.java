package com.sprint.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.agent.service.LogFileListener;
import org.apache.commons.io.input.Tailer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class AgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public CommandLineRunner startTailer(@Value("${agent.file.path}") String filePath,
                                         LogFileListener listener) {
        return args -> {
            File file = new File(filePath);

            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            System.out.println("Agent starting... watching file: " + file.getAbsolutePath());


            Tailer tailer = new Tailer(file, listener, 1000, true);

            Thread thread = new Thread(tailer);
            thread.setDaemon(false);
            thread.start();
        };
    }
}