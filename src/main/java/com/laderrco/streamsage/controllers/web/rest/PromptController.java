package com.laderrco.streamsage.controllers.web.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laderrco.streamsage.domains.Prompt;
import com.laderrco.streamsage.domains.Recommendation;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.services.Interfaces.PromptService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/prompts")
@AllArgsConstructor
public class PromptController {
    
    private final PromptService promptService;
    
    @PostMapping(value = {"","/"})
    public ResponseEntity<SuggestionPackage> sendPrompt(@RequestBody Prompt prompt) {
        // SuggestionPackage suggestionPackage = new SuggestionPackage();
        // suggestionPackage.setUserPrompt(prompt.getPrompt());
        // suggestionPackage.setTimestamp(null);
        // suggestionPackage.setRecommendationList(null);
        // return new ResponseEntity<>(suggestionPackage, HttpStatus.CREATED);

        String promptResponse = promptService.sendPrompt(prompt.getPrompt());
        System.out.println(promptResponse);
        SuggestionPackage suggestionPackage = new SuggestionPackage();
        suggestionPackage.setUserPrompt(prompt.getPrompt());

        Recommendation recommendation = new Recommendation();
        recommendation.setDescription(promptResponse);
        recommendation.setTitle("Example Title");
        suggestionPackage.setTimestamp(1234567890L);
        suggestionPackage.setRecommendationList(List.of(recommendation));
        
        return new ResponseEntity<>(suggestionPackage, HttpStatus.CREATED);
    }
}
