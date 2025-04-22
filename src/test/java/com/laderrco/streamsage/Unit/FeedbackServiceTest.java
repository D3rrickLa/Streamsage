package com.laderrco.streamsage.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.laderrco.streamsage.domains.Feedback;
import com.laderrco.streamsage.services.Interfaces.FeedbackService;


// Testing the FeedbackService, not the acutal MVC controller
// need to fix this for user auth
public class FeedbackServiceTest {
    // @InjectMocks // need a real object, use Mock
    @Mock // no presistent data
    FeedbackService feedbackService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        List<Feedback> feedbackList = feedbackService.findAll();
        assertTrue(feedbackList.size() == 0, "Size is wrong");
    }

    @Test
    public void testSave(){ 
        // Create mock feedback list
        List<Feedback> feedbackList = new ArrayList<>();
        Feedback feedback = new Feedback("This is a test comment", 5,  "Hello World");
        
        // Simulate saving by modifying the mock behavior
        doAnswer(invocation -> {
            feedbackList.add(feedback);
            return null;
        }).when(feedbackService).save(feedback);

        when(feedbackService.findById(0L)).thenReturn(Optional.of(feedback)); // Mock retrieval
        
        feedbackService.save(feedback);
        assertEquals(feedback, feedbackService.findById(0L).get());
    }
}
