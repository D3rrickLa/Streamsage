package com.laderrco.streamsage.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.laderrco.streamsage.entities.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
    public List<Feedback> findByOrderByTimestampDesc();
}
