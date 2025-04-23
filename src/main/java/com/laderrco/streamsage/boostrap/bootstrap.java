package com.laderrco.streamsage.boostrap;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.laderrco.streamsage.domains.AvailableService;
import com.laderrco.streamsage.domains.Recommendation;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.domains.Enums.RecommendationType;
import com.laderrco.streamsage.entities.AuthenticationRequest;
import com.laderrco.streamsage.entities.AuthenticationResponse;
import com.laderrco.streamsage.entities.Feedback;
import com.laderrco.streamsage.entities.User;
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
        // System.out.println(reponse);
        User user = userRepository.findByEmail("test@sample.com").get();

        AvailableService service1 = new AvailableService("Netflix", "www.netflix.com");
        AvailableService service2 = new AvailableService("Prime Video", "www.prime.com");

        Recommendation recommendation1 = new Recommendation(
            "Blacklists", RecommendationType.MOVIE, "The blacklist"
        );
        Recommendation recommendation2 = new Recommendation(
            "Blacklists2", RecommendationType.MOVIE, "The blacklist2"
        );

        SuggestionPackage suggestionPackage = new SuggestionPackage("Test Prompt", 1234567890L, List.of(recommendation1, recommendation2));
    
        Feedback feedback1 = Feedback.builder()
            .comment("Some Comment")
            .suggestionPackage(suggestionPackage)
            .user(user)
            .rating(5)
            .build();

        Feedback feedback2 = Feedback.builder()
            .comment("Some Comment 2")
            .suggestionPackage(suggestionPackage)
            .user(user)
            .rating(5)
            .build();

        feedbackService.save(feedback1);
        feedbackService.save(feedback2);
    }
    
}
