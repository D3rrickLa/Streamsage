package com.laderrco.streamsage.services;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.laderrco.streamsage.services.Interfaces.AIResponseService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AIResponseServiceImpl implements AIResponseService{
    
    private final RestTemplate restTemplate;
    
    @Value("${env.python_endpoint}")
    private String url; 

    public AIResponseServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory(); //HTTP/1.1
        this.restTemplate = restTemplateBuilder
            .requestFactory(() -> requestFactory)
            .build();
    }

    public AIResponseServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AIResponseServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String sendPrompt(String prompt) {
        
        Map<String, String> requestBody = Map.of("prompt", prompt);
        String responseBody = restTemplate.postForObject(URI.create(url), requestBody, String.class);
        return responseBody;

    }
    
}
