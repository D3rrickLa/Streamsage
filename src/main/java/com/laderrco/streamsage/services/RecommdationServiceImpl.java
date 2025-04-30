package com.laderrco.streamsage.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.domains.AvailableService;
import com.laderrco.streamsage.domains.Prompt;
import com.laderrco.streamsage.domains.Recommendation;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.domains.DTOs.MovieInfoDTO;
import com.laderrco.streamsage.domains.Enums.Genre;
import com.laderrco.streamsage.domains.Enums.RecommendationType;
import com.laderrco.streamsage.services.Interfaces.MediaLookupService;
import com.laderrco.streamsage.services.Interfaces.RecommendationService;
import com.laderrco.streamsage.utils.TimestampGenerator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecommdationServiceImpl implements RecommendationService {

    private final ObjectMapper objectMapper;
    private final MediaLookupService mediaLookupService;
    private final TimestampGenerator timestampGenerator;

    @Override
    public SuggestionPackage returnSuggestionPackage(Prompt prompt, String aiResponse) throws JsonMappingException, JsonProcessingException {
        
        Map<String, String> responseMap = objectMapper.readValue(aiResponse,  new TypeReference<Map<String, String>>() {});
        List<Recommendation> recommendationList = new ArrayList<>();

        // For matching numbered book titles
        // regex is only useful for more than n items 
        // Need to fix this regex is really bad for this, ened a more structure way
        // should probably just make a utils class for this pattern stripping
        // String regex = "^(?:\\d+\\.\\s*)?(.*?)$";
        String regex = "\\d+\\.\\s*(.*?)\\s*(?:\\n|$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(responseMap.get("response"));

        while (matcher.find()) {
            String title = matcher.group(1).trim(); // Extract title
            String sanitizedTitle = title.replaceAll("[\"()]", "").trim();
            ResponseEntity<String> tmdbResponseBody = mediaLookupService.apiResponse(sanitizedTitle);
                        
            MovieInfoDTO movieInfoDTO = mediaLookupService.getBestMatchDto(tmdbResponseBody.getBody(), sanitizedTitle);
            if (movieInfoDTO != null) {
                
                // Create Recommendation object
                Recommendation recommendation = new Recommendation();
                recommendation.setTitle(movieInfoDTO.getOriginalTitle());
                recommendation.setDescription(movieInfoDTO.getOverview());
                recommendation.setRecommendationType(RecommendationType.MEDIA); 
                
                List<Genre> genreIds = movieInfoDTO.getGenres().stream()
                    .map(Genre::fromId)
                    .filter(Objects::nonNull)
                    .toList();
                recommendation.setGenres(genreIds);
                recommendation.setReleaseDate(movieInfoDTO.getReleaseDate());

                List<AvailableService> availableServices = mediaLookupService.getListOfServices(movieInfoDTO.getId());
                if (availableServices != null) {
                    recommendation.setAvailableService(availableServices);
                }
    
                recommendationList.add(recommendation);
                
            }
        }

        SuggestionPackage suggestionPackage = new SuggestionPackage();
        suggestionPackage.setUserPrompt(prompt.getPrompt());
        suggestionPackage.setTimestamp(timestampGenerator.getTimestampUTC());
        suggestionPackage.setRecommendationList(recommendationList);

        return suggestionPackage;
    }
    
}
