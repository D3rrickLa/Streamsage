package com.laderrco.streamsage.controllers.web.rest;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.laderrco.streamsage.domains.Prompt;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.services.LocaleService;
import com.laderrco.streamsage.services.Interfaces.AIResponseService;
import com.laderrco.streamsage.services.Interfaces.RecommendationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/prompts")
@AllArgsConstructor
public class PromptController {
    
    private final AIResponseService aiResponseService;
    private final RecommendationService recommendationService;
    private final LocaleService localeService;
    
    @PostMapping(value = {"","/"})
    public ResponseEntity<SuggestionPackage> sendPrompt(@RequestBody Prompt prompt, @RequestHeader("Accept-Language") Locale locale) throws JsonMappingException, JsonProcessingException {

        String promptResponse = aiResponseService.sendPrompt(prompt.getPrompt());
        // System.out.println(promptResponse);
        System.out.println(localeService.getUserLocale());

        return new ResponseEntity<>(recommendationService.returnSuggestionPackage(prompt, promptResponse), HttpStatus.CREATED);
    }
}
