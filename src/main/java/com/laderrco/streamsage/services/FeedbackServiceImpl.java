package com.laderrco.streamsage.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
    private final JwtService jwtService;

    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        return feedbackRepository.findById(id);
    }

    @Override
    public Feedback save(Feedback feedback) {
        feedback.setTimestamp(timestampGenerator.getTimestampUTC());
        return feedbackRepository.save(feedback);
    }
    
    @Override
    public Feedback submitFeedback(String jwtToken, Feedback feedback) {
        String cleanedToken = jwtToken.replace("Bearer ", "").trim();
        String email = jwtService.extractUsername(cleanedToken);
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        feedback.setTimestamp(timestampGenerator.getTimestampUTC());
        feedback.setUser(user);
        return feedbackRepository.save(feedback);
    }
}
