package com.laderrco.streamsage.services;

import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.laderrco.streamsage.services.Interfaces.AIResponseService;

@Service
public class AIResponseServiceImpl implements AIResponseService{
    
    private final RestTemplate restTemplate;
    final String url = "http://localhost:50001/api/v1/generate"; 

    public AIResponseServiceImpl(RestTemplateBuilder restTemplate) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory(); //HTTP/1.1
        this.restTemplate = restTemplate
            .requestFactory(() -> requestFactory)    
            .build();
    }

    @Override
    public String sendPrompt(String prompt) {
        
        Map<String, String> requestBody = Map.of("prompt", prompt);
        String responseBody = restTemplate.postForObject(url, requestBody, String.class);
        return responseBody;

    }
    
}
