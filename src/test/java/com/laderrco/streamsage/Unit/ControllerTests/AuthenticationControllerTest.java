package com.laderrco.streamsage.Unit.ControllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.configuration.PasetoAuthenticationFilter;
import com.laderrco.streamsage.controllers.web.rest.AuthenticationController;
import com.laderrco.streamsage.dtos.AuthenticationRequest;
import com.laderrco.streamsage.dtos.AuthenticationResponse;
import com.laderrco.streamsage.services.Interfaces.AuthenticationService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = "USER")
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private PasetoAuthenticationFilter pasetoAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper; // Serialize objects to a format of your desire


    // runs before each test method for setup -> override its behaviour and make sure it doesn't interfer with handle response
    @BeforeEach
    void setup() throws ServletException, IOException {
        doAnswer(invocation -> {
            // Simply continue the filter chain without altering the response
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(pasetoAuthenticationFilter).doFilter(any(), any(), any());
    }


    // test if registering a user works
    @Test
    void testRegister_Correct() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("email@email.com", "1234");

        when(authenticationService.register(authenticationRequest)).thenReturn(new AuthenticationResponse("some_token"));

        mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .content(objectMapper.writeValueAsString(authenticationRequest))) // Convert object to JSON string
            .andExpect(status().isOk())    
            .andExpect(jsonPath("$.token").value("some_token")); // Check response content    
    }

    @Test
    void testRegister_Incorrect() throws Exception {
        AuthenticationRequest existingUserRequest = new AuthenticationRequest("existing_user@email.com", "secure_password");

        when(authenticationService.register(existingUserRequest))
            .thenThrow(new IllegalStateException("Email already registered"));
    
        mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .content(objectMapper.writeValueAsString(existingUserRequest)))
            .andExpect(status().isConflict()) // Expecting 409 Conflict
            .andExpect(jsonPath("$.message").value("Email already registered"));
    }


    @Test
    void testAuthenticate_CorrectCredentials() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("existing_user@email.com", "secure_password");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("valid_token");
        
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(expectedResponse);
        
        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .with(csrf())
        .content(objectMapper.writeValueAsString(authenticationRequest)))
        .andExpect(status().isOk())
        .andReturn();

        AuthenticationResponse actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AuthenticationResponse.class);

        assertEquals(expectedResponse, actualResponse); // Direct object comparison
    }
}
