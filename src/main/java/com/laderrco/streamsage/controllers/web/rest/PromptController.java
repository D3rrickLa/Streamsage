package com.laderrco.streamsage.controllers.web.rest;

import java.io.IOException;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laderrco.streamsage.domains.Prompt;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.services.RedisCacheService;
import com.laderrco.streamsage.services.TokenService;
import com.laderrco.streamsage.services.Interfaces.AIResponseService;
import com.laderrco.streamsage.services.Interfaces.RecommendationService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/prompts")
@AllArgsConstructor
public class PromptController {
    
    private final AIResponseService aiResponseService;
    private final RecommendationService recommendationService;
    private final RedisCacheService redisCacheService;
    private final TokenService tokenService;

    @GetMapping(value = {"","/"})
    public ResponseEntity<String> hearbeat() {
        return ResponseEntity.ok("Server is up and runnin");
    }
    
    @PostMapping(value = {"","/"}, consumes="application/json")
    public ResponseEntity<SuggestionPackage> sendPrompt(
        HttpSession session, @RequestBody Prompt prompt, @RequestHeader("Accept-Language") Locale locale, 
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader) throws IOException {

        // call redis here
        SuggestionPackage suggestionPackage = redisCacheService.fetchFromRedis(prompt.getPrompt());
        if (suggestionPackage != null) {
            checkUserHasBearer(session, authorizationHeader, suggestionPackage);
            return new ResponseEntity<>(suggestionPackage, HttpStatus.CREATED);
        }
        
        String promptResponse = aiResponseService.sendPrompt(prompt.getPrompt());
        // System.out.println(promptResponse);
        suggestionPackage = recommendationService.returnSuggestionPackage(prompt, promptResponse);
        if (suggestionPackage.getRecommendationList() != null) {
            redisCacheService.saveToCache(prompt.getPrompt(), suggestionPackage);
            checkUserHasBearer(session, authorizationHeader, suggestionPackage);
            return new ResponseEntity<>(suggestionPackage, HttpStatus.CREATED);
            
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void checkUserHasBearer(HttpSession session, String authorizationHeader, SuggestionPackage suggestionPackage) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                if (tokenService.decrypt(token) != null) {
                    session.setAttribute("suggestionPackage", suggestionPackage);             
                }
            }
    }
}
