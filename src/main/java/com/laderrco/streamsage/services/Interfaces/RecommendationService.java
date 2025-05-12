package com.laderrco.streamsage.services.Interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.laderrco.streamsage.domains.Prompt;
import com.laderrco.streamsage.domains.SuggestionPackage;

public interface RecommendationService {
    public SuggestionPackage returnSuggestionPackage(Prompt prompt, String aiResponse) throws JsonMappingException, JsonProcessingException;
}
