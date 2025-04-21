package com.laderrco.streamsage.services.Interfaces;

import java.util.List;
import java.util.Optional;

import com.laderrco.streamsage.domains.Feedback;

public interface FeedbackService {
    public List<Feedback> findAll();
    public Optional<Feedback> findById(Long id);
    public Feedback save (Feedback feedback);
}
