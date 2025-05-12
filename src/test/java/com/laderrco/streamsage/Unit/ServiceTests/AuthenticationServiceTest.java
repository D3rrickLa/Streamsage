package com.laderrco.streamsage.Unit.ServiceTests;

import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.laderrco.streamsage.domains.Enums.Roles;
import com.laderrco.streamsage.dtos.AuthenticationRequest;
import com.laderrco.streamsage.dtos.AuthenticationResponse;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.repositories.UserRepository;
import com.laderrco.streamsage.services.AuthenticationServiceImpl;
import com.laderrco.streamsage.services.TokenService;

public class AuthenticationServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;


    private AuthenticationServiceImpl authenticationService;

    private User user;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userRepository, authenticationManager, passwordEncoder, tokenService);
    
        
        user = User.builder()
            .id(1L)
            .email("email123@example.com")
            .password("password123456")
            .role(Roles.ROLE_USER)
            .build();

    }


    @Test
    void testRegister_Correct() {
        tokenService.testSetAPIandFooter();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("email123@example.com", "password123456");

        AuthenticationResponse authenticationResponse = authenticationService.register(authenticationRequest);

        assertNotNull(authenticationResponse);

    }

    @Test
    void testAuthenticate_Correct() {
        tokenService.testSetAPIandFooter();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("email123@example.com", "password123456");
        authenticationService.register(authenticationRequest);


        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        assertNotNull(authenticationResponse);
        assertEquals(authenticationRequest.getEmail(), user.getUsername());



    }
}
