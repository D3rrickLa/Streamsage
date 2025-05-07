package com.laderrco.streamsage.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.dtos.FeedbackDTO;
import com.laderrco.streamsage.entities.Feedback;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.repositories.FeedbackRepository;
import com.laderrco.streamsage.services.Interfaces.FeedbackService;
import com.laderrco.streamsage.services.Interfaces.UserService;
import com.laderrco.streamsage.utils.TimestampGenerator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final TimestampGenerator timestampGenerator;

    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        return feedbackRepository.findById(id);
    }

    @Override 
    @Deprecated // remove this
    public Feedback save(Feedback feedback) {
        feedback.setTimestamp(timestampGenerator.getTimestampUTC());
        return feedbackRepository.save(feedback);
    }
    
    @Override
    public Feedback submitFeedback(FeedbackDTO feedbackDTO, SuggestionPackage suggestionPackage) {
        
        User user  = userService.findById(userService.findIdByEmail()).orElseThrow();
        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setComment(feedbackDTO.getComment());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setSuggestionPackage(suggestionPackage);
        feedback.setTimestamp(timestampGenerator.getTimestampUTC());
        return feedbackRepository.save(feedback);
    }
}
