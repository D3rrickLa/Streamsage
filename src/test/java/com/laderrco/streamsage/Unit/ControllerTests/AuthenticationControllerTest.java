package com.laderrco.streamsage.Unit.ControllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.laderrco.streamsage.controllers.web.rest.AuthenticationController;
import com.laderrco.streamsage.domains.AuthenticationRequest;
import com.laderrco.streamsage.domains.AuthenticationResponse;

import com.laderrco.streamsage.services.AuthenticationService;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    public void testRegisterUser_Success() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");
        AuthenticationResponse response = new AuthenticationResponse("mockToken");

        when(authenticationService.register(any(AuthenticationRequest.class))).thenReturn(response);
        mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"email\":\"test@example.com\", \"password\":\"password123\"}"))
        .andExpect(status().isOk());
        .andExpect(jsonPath("$.token").value("mockToken"));
    }
}
