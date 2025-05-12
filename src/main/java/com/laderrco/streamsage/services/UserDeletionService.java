package com.laderrco.streamsage.services;

import org.springframework.stereotype.Service;

import com.laderrco.streamsage.dtos.AuthenticationRequest;
import com.laderrco.streamsage.services.Interfaces.FeedbackService;
import com.laderrco.streamsage.services.Interfaces.UserService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserDeletionService {

    private final UserService userService;
    private final FeedbackService feedbackService;

    @Transactional
    public void deleteUserInfo(AuthenticationRequest authenticationRequest) throws Exception {
        feedbackService.deleteByUserId(userService.findIdByEmail());
        userService.delete(authenticationRequest);
    }
}
