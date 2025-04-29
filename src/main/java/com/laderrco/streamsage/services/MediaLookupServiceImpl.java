package com.laderrco.streamsage.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.laderrco.streamsage.services.Interfaces.MediaLookupService;

@Service
public class MediaLookupServiceImpl implements MediaLookupService{
    
    @Value("${env.TMDB_KEY}")
    private String API_KEY;

    @Override
    public ResponseEntity<String> apiResponse(String mediaName) {        
        String url = "https://api.themoviedb.org/3/search/movie?query=Inception"; // Example
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return ResponseEntity.ok(response.getBody()); // Returning TMDb response
        

    }
    
}
