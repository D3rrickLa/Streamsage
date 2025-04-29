package com.laderrco.streamsage.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;


// NOTE: this is to be removed when we push to docker
@Component
public class PythonServiceManager {
    
    private Process pythonProcess;
    
    @PostConstruct
    public void startPythonService() {
        try {
            ProcessBuilder builder = new ProcessBuilder("python", "ai_model_api.py");
            builder.directory(new File("ai-model-python"));
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            builder.redirectError(ProcessBuilder.Redirect.INHERIT);
            pythonProcess = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Python Output: " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopPythonService() {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            pythonProcess.destroy();
        }
    }
}
