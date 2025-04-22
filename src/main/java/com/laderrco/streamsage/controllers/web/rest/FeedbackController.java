package com.laderrco.streamsage.controllers.web.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laderrco.streamsage.domains.Feedback;
import com.laderrco.streamsage.services.Interfaces.FeedbackService;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {
   
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping(value = {"", "/"})
    public List<Feedback> getAllFeedback() {
        return feedbackService.findAll();
    }

    @GetMapping(value="{id}")
    public Optional<Feedback> getIndividualFeedback(@PathVariable("id") Long id) {
        return feedbackService.findById(id);
    }

    @PostMapping(value = {"","/"})
    public Feedback saveFeedback(@RequestHeader("Authorization") String token, @RequestBody Feedback feedback) {
        return feedbackService.submitFeedback(token, feedback);
    }

}
