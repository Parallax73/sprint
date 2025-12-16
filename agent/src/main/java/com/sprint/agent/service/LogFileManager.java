package com.sprint.agent.service;

import com.sprint.agent.config.AgentConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input. Tailer;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file. Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogFileManager {

    private final AgentConfiguration config;
    private final LogFileListenerFactory listenerFactory;
    private final List<Tailer> activeTailers = new ArrayList<>();

    public void startWatching() {
        log.info("📂 Configuring log file watchers...");

        for (AgentConfiguration.LogFileConfig fileConfig : config.getFiles()) {
            if (! fileConfig.isEnabled()) {
                log.info("⏭️  Skipping disabled file: {}", fileConfig.getPath());
                continue;
            }

            File file = resolveFile(fileConfig.getPath());

            if (! file.exists()) {
                log.warn("⚠️  File does not exist: {}", file. getAbsolutePath());
                log.info("Creating file:  {}", file.getAbsolutePath());
                createFile(file);
            }

            LogFileListener listener = listenerFactory.createListener(
                    fileConfig.getService(),
                    fileConfig.getType()
            );

            Tailer tailer = new Tailer(file, listener, 1000, true);
            Thread thread = new Thread(tailer, "Tailer-" + fileConfig. getService());
            thread.setDaemon(false);
            thread.start();

            activeTailers.add(tailer);

            log.info("✅ Watching:  {} [{}] → {}",
                    file.getAbsolutePath(),
                    fileConfig.getService(),
                    fileConfig.getType()
            );
        }

        log.info("✨ Agent is now monitoring {} log file(s)", activeTailers.size());
    }

    private File resolveFile(String path) {
        Path filePath = Paths.get(path);

        if (filePath.isAbsolute()) {
            return filePath.toFile();
        }

        return new File(System.getProperty("user.home"), path).getAbsoluteFile();
    }

    private void createFile(File file) {
        try {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            log.info("📝 Created new file: {}", file.getAbsolutePath());
        } catch (Exception e) {
            log.error("Failed to create file: {}", file. getAbsolutePath(), e);
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("🛑 Stopping all log watchers...");
        activeTailers.forEach(Tailer::stop);
    }
}