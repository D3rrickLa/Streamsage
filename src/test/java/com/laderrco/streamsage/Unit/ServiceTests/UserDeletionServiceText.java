package com.laderrco.streamsage.Unit.ServiceTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.laderrco.streamsage.domains.Enums.Roles;
import com.laderrco.streamsage.dtos.AuthenticationRequest;
import com.laderrco.streamsage.entities.Feedback;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.repositories.FeedbackRepository;
import com.laderrco.streamsage.repositories.UserRepository;
import com.laderrco.streamsage.services.FeedbackServiceImpl;
import com.laderrco.streamsage.services.UserDeletionService;
import com.laderrco.streamsage.services.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDeletionServiceText {
    

    @MockitoBean
    @Lazy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private UserServiceImpl userService;

    @Mock
    private FeedbackServiceImpl feedbackService;
    

    @InjectMocks
    private UserDeletionService userDeletionService;

    private User user;
    private Feedback feedback;

    @BeforeEach
    public void init() {
        user =  User.builder()
        .id(1L)
        .firstName("john")
        .lastName("smith")
        .email("john@example.com")
        .role(Roles.ROLE_USER)
        .password(passwordEncoder.encode("1234"))
        .build();

        feedback = Feedback.builder()
            .id(1L)
            .comment("some comment")
            .rating(5)
            .suggestionPackage(null)
            .timestamp(123456789L)
            .user(user)
            .build();
    }

    @Test
    void testDeleteUserInfo_Success() throws Exception {
        when(userService.findIdByEmail()).thenReturn(1L);
        willDoNothing().given(feedbackService).deleteByUserId(1L);
        willDoNothing().given(userService).delete(any());

        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "1234");


        userDeletionService.deleteUserInfo(request);

        verify(feedbackService, times(1)).deleteByUserId(feedback.getUser().getId()); // ✅ Ensures feedback deletion was called
        verify(userService, times(1)).delete(request); // ✅ Ensures user deletion was called
    }
}
