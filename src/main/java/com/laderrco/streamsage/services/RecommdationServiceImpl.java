package com.laderrco.streamsage.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.domains.Prompt;
import com.laderrco.streamsage.domains.Recommendation;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.domains.Enums.RecommendationType;
import com.laderrco.streamsage.services.Interfaces.MediaLookupService;
import com.laderrco.streamsage.services.Interfaces.RecommendationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecommdationServiceImpl implements RecommendationService {

    private final ObjectMapper objectMapper;
    private final MediaLookupService mediaLookupService;
    
    @Override
    public SuggestionPackage returnSuggestionPackage(Prompt prompt, String aiResponse) throws JsonMappingException, JsonProcessingException {
        
        Map<String, String> responseMap = objectMapper.readValue(aiResponse,  new TypeReference<Map<String, String>>() {});
        List<Recommendation> recommendationList = new ArrayList<>();

        // For matching numbered book titles
        String regex = "\\d+\\.\\s*(.*?)(?:\\n|$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(responseMap.get("response"));

        while (matcher.find()) {
            String title = matcher.group(1).trim(); // Extract title

            // Create Recommendation object
            Recommendation recommendation = new Recommendation();
            recommendation.setTitle(title);
            recommendation.setDescription("Generated book recommendation");
            recommendation.setRecommendationType(RecommendationType.MEDIA); 

            recommendationList.add(recommendation);
        }

        SuggestionPackage suggestionPackage = new SuggestionPackage();
        suggestionPackage.setUserPrompt(prompt.getPrompt());
        suggestionPackage.setTimestamp(System.currentTimeMillis());
        suggestionPackage.setRecommendationList(recommendationList);

        return suggestionPackage;
    }
    
}
