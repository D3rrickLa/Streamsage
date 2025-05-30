package com.laderrco.streamsage.services.Interfaces;

import java.util.List;
import java.util.Optional;

import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.dtos.FeedbackDTO;
import com.laderrco.streamsage.entities.Feedback;
import com.laderrco.streamsage.entities.User;

public interface FeedbackService {
    public List<Feedback> findAll();
    public List<Feedback> findAllByIndividual(User user);
    public Optional<Feedback> findById(Long id);
    public Feedback save (Feedback feedback);
    public Feedback submitFeedback(FeedbackDTO feedback, SuggestionPackage suggestionPackage);

    public void deleteByUserId(Long id);
}
