package com.laderrco.streamsage.Unit.ControllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.configuration.PasetoAuthenticationFilter;
import com.laderrco.streamsage.controllers.web.rest.UserController;
import com.laderrco.streamsage.domains.Enums.Roles;
import com.laderrco.streamsage.dtos.AuthenticationRequest;
import com.laderrco.streamsage.dtos.CredentialsDTO;
import com.laderrco.streamsage.dtos.UserInfoDTO;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.services.TokenService;
import com.laderrco.streamsage.services.UserDeletionService;
import com.laderrco.streamsage.services.Interfaces.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = "USER")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDeletionService userDeletionService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private PasetoAuthenticationFilter pasetoAuthenticationFilter;

    @BeforeEach
    void setup() throws ServletException, IOException {
        doAnswer(invocation -> {
            // Simply continue the filter chain without altering the response
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(pasetoAuthenticationFilter).doFilter(any(), any(), any());

        User testUser = User.builder()
            .email("email@example.com")
            .password("some_password")
            .role(Roles.ROLE_USER)
            .build();
        when(userService.findByEmail("existing_user@email.com")).thenReturn(Optional.of(testUser));
    }


    // tests if the user has been updated
    @Test
    void testUpdateUserInfo_Correct() throws Exception {
        User updateUser = User.builder()
            .firstName("John")
            .lastName("Test")
            .email("email@example.com")
            .password("some_password")
            .build();
        UserInfoDTO userInfoDTO = new UserInfoDTO(updateUser);

        when(userService.updateUserProfile(any(), any()))
        .thenReturn(Optional.of(updateUser).get()); // Mock successful update
        
        mockMvc.perform(put("/api/v1/accounts/profile")
        .contentType(MediaType.APPLICATION_JSON)
        .with(csrf()) // If CSRF protection is enabled
        .content(objectMapper.writeValueAsString(userInfoDTO)))
        .andExpect(status().isOk());
    }


    @Test
    void testGetUserInfo() throws Exception {
        MockHttpSession mockSession = new MockHttpSession(); 
        mockSession.setAttribute("userEmail", "testUser@email.com"); // Ensure a valid email is set

        User mockUser = User.builder()
            .firstName("John")
            .lastName("Doe")
            .email("testUser@email.com")
            .password("testing")
            .build();

        when(userService.findByEmail("testUser@email.com")).thenReturn(Optional.of(mockUser));    
        
        mockMvc.perform(get("/api/v1/accounts/profile")
            .session(mockSession) // Pass the mocked session
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.email").value("testUser@email.com"));
    }

    @Test
    void testUpdatePassword_Correct() throws Exception {
        CredentialsDTO credentialsDTO = new CredentialsDTO("old_password", "new_password");

        // Mock user lookup
        User mockUser = User.builder()
            .id(1L)
            .email("test@example.com")
            .password("old_password")
            .role(Roles.ROLE_USER)
            .build();
        when(userService.findByEmail("testUser")).thenReturn(Optional.of(mockUser));

        doNothing().when(userService).updateUserPassword(eq(1L), any(CredentialsDTO.class));

        mockMvc.perform(put("/api/v1/accounts/password")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()) // Keep CSRF enabled
            .content(objectMapper.writeValueAsString(credentialsDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string("Password has been updated successfully"));
    }

    @Test
    void testDeleteUser_Correct() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUser@email.com", "user_password");

        // Mock deletion logic
        doNothing().when(userDeletionService).deleteUserInfo(any(AuthenticationRequest.class));

        mockMvc.perform(post("/api/v1/accounts/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .content(objectMapper.writeValueAsString(authenticationRequest)))
            .andExpect(status().isOk())
            .andExpect(content().string("User has been deleted"));

        // Verify that user deletion was called
        verify(userDeletionService, times(1)).deleteUserInfo(authenticationRequest);
    }

}
