package com.laderrco.streamsage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.domains.DTOs.MovieInfoDTO;
import com.laderrco.streamsage.services.Interfaces.MediaLookupService;

@Service
public class MediaLookupServiceImpl implements MediaLookupService{
    
    @Value("${env.TMDB_KEY}")
    private String API_KEY;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Autowired
    public MediaLookupServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();   
    }

    // NOTE: the api when doing the search like this isn't that accurate.
    // it gives you everything with the work 'meidaName' in it
    @Override
    public ResponseEntity<String> apiResponse(String mediaName) {    
        String url = "https://api.themoviedb.org/3/search/movie?query="+mediaName+"&language=en-US&page=1"; // Example
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @Override
    public MovieInfoDTO getBestMatchDto(String jsonResponse, String mediaName) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultNode = rootNode.get("results"); 
            
            if (resultNode != null && resultNode.isArray()) {
                for (JsonNode movieNode : resultNode) {
                    MovieInfoDTO movie = objectMapper.treeToValue(movieNode, MovieInfoDTO.class);
                    if (movie.getOriginalTitle().equalsIgnoreCase(mediaName)) {
                        return movie; //exact match
                    }
                }
                return objectMapper.treeToValue(resultNode.get(0), MovieInfoDTO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
