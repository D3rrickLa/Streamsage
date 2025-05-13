package com.laderrco.streamsage.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

// // NOTE: this is to be removed when we push to docker
@Component
@Deprecated
public class PythonServiceManager {
    
    private Process pythonProcess;
    
    @PostConstruct
    public void startPythonService() {
        startPythonProcess();
        watchPythonScript();
    }

    private void startPythonProcess() {
        try {
            ProcessBuilder builder = new ProcessBuilder("uvicorn", "ai_model_api:app", "--host", "localhost", "--port", "50001");
            builder.directory(new File("ai-model-python\\app"));
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            builder.redirectError(ProcessBuilder.Redirect.INHERIT);
            pythonProcess = builder.start();
    
            // Run output logging in a separate thread to prevent blocking
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Python Output: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void watchPythonScript() {
        Thread watcherThread = new Thread(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                Path path = Paths.get("ai-model-python");
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.context().toString().equals("ai_model_api.py")) {
                            System.out.println("Detected update in ai_model_api.py. Restarting Python service...");
                            startPythonProcess();
                        }
                    }
                    key.reset();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        watcherThread.setDaemon(true);
        watcherThread.start();
    }

    @PreDestroy
    public void stopPythonService() {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            pythonProcess.destroy();
        }
    }
}
