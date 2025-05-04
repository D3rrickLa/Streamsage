package com.laderrco.streamsage.boostrap;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.laderrco.streamsage.domains.AvailableService;
import com.laderrco.streamsage.domains.Recommendation;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.domains.Enums.RecommendationType;
import com.laderrco.streamsage.domains.Enums.Roles;
import com.laderrco.streamsage.entities.Feedback;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.services.Interfaces.FeedbackService;
import com.laderrco.streamsage.services.Interfaces.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class bootstrap implements CommandLineRunner {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        User user01 = User.builder()
            .email("john@sample.com")
            .password("1234")
            .role(Roles.USER)
            .build();

        User user02 = User.builder()
            .email("john2@sample.com")
            .password("1234")
            .role(Roles.USER)
            .build();

        userService.save(user01);
        userService.save(user02);

        AvailableService service1 = AvailableService.builder()
            .URL("www.netflix.com")
            .id(01L)
            .name("Netflix")
            .logoURL("http://")
            .type("Rent/Buy")
            .build();

        AvailableService service2 = AvailableService.builder()
            .URL("www.primevideo.com")
            .id(02L)
            .name("Prime Video")
            .logoURL("http://")
            .type("Rent/Buy")
            .build();
  

        Recommendation recommendation1 = new Recommendation(
            "Blacklists", RecommendationType.MOVIE, "The blacklist", null, null, List.of(service1, service2)
        );
        Recommendation recommendation2 = new Recommendation(
            "Blacklists2", RecommendationType.MOVIE, "The blacklist2", null, null, List.of(service1)
        );

        SuggestionPackage suggestionPackage = new SuggestionPackage("Test Prompt", 1234567890L, List.of(recommendation1, recommendation2));
    
        Feedback feedback1 = Feedback.builder()
            .comment("Some Comment")
            .suggestionPackage(suggestionPackage)
            .rating(5)
            .user(user01)
            .build();

        Feedback feedback2 = Feedback.builder()
            .comment("Some Comment 2")
            .suggestionPackage(suggestionPackage)
            .rating(5)
            .user(user02)
            .build();

        feedbackService.save(feedback1);
        feedbackService.save(feedback2);
    }
    
}
