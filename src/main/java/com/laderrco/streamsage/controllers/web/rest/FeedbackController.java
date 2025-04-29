package com.laderrco.streamsage.controllers.web.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laderrco.streamsage.entities.Feedback;
import com.laderrco.streamsage.services.Interfaces.FeedbackService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/feedbacks")
@AllArgsConstructor
public class FeedbackController {
   
    private final FeedbackService feedbackService;

    @GetMapping(value = {"", "/"})
    public List<Feedback> getAllFeedback() {
        return feedbackService.findAll();
    }

    @GetMapping(value="{id}")
    public Optional<Feedback> getIndividualFeedback(@PathVariable("id") Long id) {
        return feedbackService.findById(id);
    }

    @PostMapping(value = {"","/"})
    public Feedback saveFeedback(@RequestBody Feedback feedback) {
        // we can use sessions for the prompt input
        return feedbackService.submitFeedback(feedback);
    }

}
