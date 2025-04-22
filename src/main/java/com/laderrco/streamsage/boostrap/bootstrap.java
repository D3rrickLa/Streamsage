package com.laderrco.streamsage.boostrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.laderrco.streamsage.domains.AuthenticationRequest;
import com.laderrco.streamsage.domains.AuthenticationResponse;
import com.laderrco.streamsage.domains.Feedback;
import com.laderrco.streamsage.domains.User;
import com.laderrco.streamsage.repositories.UserRepository;
import com.laderrco.streamsage.services.AuthenticationService;
import com.laderrco.streamsage.services.Interfaces.FeedbackService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class bootstrap implements CommandLineRunner {

    private final AuthenticationService authenticationService;
    private final FeedbackService feedbackService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("test@sample.com", "password1");
        AuthenticationResponse reponse = authenticationService.register(authenticationRequest);
        User user = userRepository.findByEmail("test@sample.com").get();

        Feedback feedback1 = Feedback.builder()
            .comment("Some Comment")
            .recommendationList("Testing should be a list")
            .user(user)
            .rating(5)
            .build();

        Feedback feedback2 = Feedback.builder()
            .comment("Some Comment 2")
            .recommendationList("Testing should be a list")
            .user(user)
            .rating(5)
            .build();

        feedbackService.save(feedback1);
        feedbackService.save(feedback2);
    }
    
}
