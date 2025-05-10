package com.laderrco.streamsage.controllers.web.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.dtos.FeedbackDTO;
import com.laderrco.streamsage.entities.Feedback;
import com.laderrco.streamsage.services.Interfaces.FeedbackService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/feedbacks")
@AllArgsConstructor
public class FeedbackController {
   
    private final FeedbackService feedbackService;

    @GetMapping(value = {"", "/"}) // limit this by role
    @PreAuthorize("hasRole('ADMIN')") // Explicitly use ROLE_ prefix
    public List<Feedback> getAllFeedback() {
        return feedbackService.findAll();
    }
    
    @GetMapping(value="{id}") // limit this by role
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Optional<Feedback> getIndividualFeedback(@PathVariable("id") Long id) {
        return feedbackService.findById(id);
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<Feedback> saveFeedback(HttpSession session, @RequestBody FeedbackDTO feedback) {
        SuggestionPackage suggestionPackage = (SuggestionPackage) session.getAttribute("suggestionPackage");

        if (suggestionPackage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Handle case where package isn't found
        }
    
        // we can use sessions for the prompt input
        session.removeAttribute("suggestionPackage"); 
        return ResponseEntity.ok(feedbackService.submitFeedback(feedback, suggestionPackage));
    }

}
