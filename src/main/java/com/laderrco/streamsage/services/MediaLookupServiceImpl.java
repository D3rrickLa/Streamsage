package com.laderrco.streamsage.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import com.laderrco.streamsage.domains.AvailableService;
import com.laderrco.streamsage.dtos.AvailableServiceDTO;
import com.laderrco.streamsage.dtos.MovieInfoDTO;
import com.laderrco.streamsage.services.Interfaces.MediaLookupService;


@Service
public class MediaLookupServiceImpl implements MediaLookupService{
    
    @Value("${env.token.TMDB_KEY}")
    private String API_KEY;

    private final ObjectMapper objectMapper;
    private final LocaleService localeService;
    private final RestTemplate restTemplate;


    @Autowired
    public MediaLookupServiceImpl(RestTemplateBuilder builder, LocaleService localeService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = builder.build();   
        this.localeService = localeService;
    }

    // NOTE: the api when doing the search like this isn't that accurate.
    // it gives you everything with the work 'meidaName' in it
    @Override
    public ResponseEntity<String> apiResponse(String mediaName) {    
        String url = "https://api.themoviedb.org/3/search/movie?query="+mediaName+"&language="+localeService.getUserLocale().toString().replace('_', '-')+"&page=1"; // Example
        
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
            JsonNode resultsNode = rootNode.get("results"); 
            if (resultsNode != null && resultsNode.isArray()) {
                JsonNode bestMatchJsonNode = resultsNode.get(0);
                int highestVoltCount = 0;

                for (JsonNode movieNode : resultsNode) {
                    JsonNode voteCountNode = movieNode.get("vote_count");  
                    int voteCount = (voteCountNode != null) ? voteCountNode.asInt() : 0; // Default to 0 if missing
                    if(voteCount > highestVoltCount) {
                        bestMatchJsonNode = movieNode;
                        highestVoltCount = voteCount;
                    }
                }
                return objectMapper.treeToValue(bestMatchJsonNode, MovieInfoDTO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AvailableService> getListOfServices(Long id) {
        if (id == null) {
            return Collections.emptyList();
        }
        String url = String.format("https://api.themoviedb.org/3/movie/%d/watch/providers", id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(API_KEY);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        
        List<AvailableServiceDTO> dtos = new ArrayList<>();
       
        try {
           JsonNode rootNode = objectMapper.readTree(responseBody.getBody());
           JsonNode localeProviders = rootNode.path("results").path(localeService.getUserLocale().getCountry());
           if (!localeProviders.isMissingNode()) {
                dtos.addAll(extractProviderDtos(localeProviders.path("rent")));
                dtos.addAll(extractProviderDtos(localeProviders.path("buy")));
           }

           return convertDtoToDomain(dtos, "rent/buy");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    
    private List<AvailableServiceDTO> extractProviderDtos(JsonNode providerList) {
        List<AvailableServiceDTO> dtos = new ArrayList<>();
        
        if (providerList != null && providerList.isArray()) {
            for (JsonNode providerNode : providerList) {
                AvailableServiceDTO dto = new AvailableServiceDTO();
                dto.setProviderId(providerNode.get("provider_id").asLong());
                dto.setProviderName(providerNode.get("provider_name").asText());
                dto.setLogoPath(providerNode.get("logo_path").asText());

                dtos.add(dto);
            }
        }
        return dtos;
    }

    private List<AvailableService> convertDtoToDomain(List<AvailableServiceDTO> dtos, String type) {
        return dtos.stream()
            .map(dto -> new AvailableService(dto.getProviderId(), dto.getProviderName(), dto.getLogoPath(), type))
            .toList();
    }
}
