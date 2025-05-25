package com.laderrco.streamsage.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import com.laderrco.streamsage.domains.Enums.Genre;
import com.laderrco.streamsage.domains.Enums.RecommendationType;
import com.laderrco.streamsage.dtos.MovieInfoDTO;
import com.laderrco.streamsage.services.Interfaces.MediaLookupService;
import com.laderrco.streamsage.services.Interfaces.RecommendationService;
import com.laderrco.streamsage.utils.TimestampGenerator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final ObjectMapper objectMapper;
    private final MediaLookupService mediaLookupService;
    private final TimestampGenerator timestampGenerator;

    @Override
    public SuggestionPackage returnSuggestionPackage(Prompt prompt, String aiResponse) throws JsonMappingException, JsonProcessingException {
        
        Map<String, List<String>> responseMap = objectMapper.readValue(aiResponse, new TypeReference<Map<String, List<String>>>() {});        
        List<Recommendation> recommendationList = new ArrayList<>();
        
        for (String title : responseMap.get("message")) {
            ResponseEntity<String> tmdbResponseBody = mediaLookupService.apiResponse(title);

            MovieInfoDTO movieInfoDTO = mediaLookupService.getBestMatchDto(tmdbResponseBody.getBody(), title);
            if (movieInfoDTO == null) {
                continue;
            }

            List<AvailableService> availableServices = mediaLookupService.getListOfServices(movieInfoDTO.getId());
            if (availableServices == null) {
                continue;
            }

            Recommendation recommendation = Recommendation.builder()
                .title(movieInfoDTO.getOriginalTitle())
                .description(movieInfoDTO.getOverview())
                .posterURL(movieInfoDTO.getPosterPath())
                .recommendationType(RecommendationType.MEDIA)
                .genres(movieInfoDTO.getGenres().stream()
                    .map(Genre::fromId)
                    .filter(Objects::nonNull)
                    .toList()
                )
                .availableService(availableServices)
                .releaseDate(movieInfoDTO.getReleaseDate())
                .build();
            recommendationList.add(recommendation);

        }
        
        SuggestionPackage suggestionPackage = new SuggestionPackage();
        suggestionPackage.setUserPrompt(prompt.getPrompt());
        suggestionPackage.setTimestamp(timestampGenerator.getTimestampUTC());
        suggestionPackage.setRecommendationList(recommendationList);

        return suggestionPackage;
    }
    
}
