package com.laderrco.streamsage.services;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.laderrco.streamsage.services.Interfaces.AIResponseService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AIResponseServiceImpl implements AIResponseService{
    
    private final RestTemplate restTemplate = new RestTemplate();
    final String url = "http://localhost:50001/generate";

    @Override
    public String sendPrompt(String prompt) {
        
        Map<String, String> requestBody = Map.of("prompt", prompt);
        String responseBody = restTemplate.postForObject(url, requestBody, String.class);
        return responseBody;

    }
    
}
