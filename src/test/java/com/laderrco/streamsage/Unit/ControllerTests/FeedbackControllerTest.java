package com.laderrco.streamsage.Unit.ControllerTests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.configuration.PasetoAuthenticationFilter;
import com.laderrco.streamsage.controllers.web.rest.FeedbackController;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.dtos.FeedbackDTO;
import com.laderrco.streamsage.entities.Feedback;
import com.laderrco.streamsage.services.Interfaces.FeedbackService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FeedbackController.class)
@AutoConfigureMockMvc
public class FeedbackControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PasetoAuthenticationFilter pasetoAuthenticationFilter;

    @MockitoBean
    private FeedbackService feedbackService;

    @BeforeEach
    void setup() throws ServletException, IOException {
        doAnswer(invocation -> {
            // Simply continue the filter chain without altering the response
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(pasetoAuthenticationFilter).doFilter(any(), any(), any());
    }

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void testSaveFeedback_Correct() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
    
        // Simulate session storing a suggestion package
        SuggestionPackage mockPackage = new SuggestionPackage();
        mockSession.setAttribute("suggestionPackage", mockPackage);

        FeedbackDTO feedbackDTO = new FeedbackDTO(5, "Great suggestion!");
        Feedback mockFeedback = new Feedback(feedbackDTO.getComment(), feedbackDTO.getRating(), mockPackage);

        when(feedbackService.submitFeedback(any(FeedbackDTO.class), any(SuggestionPackage.class)))
            .thenReturn(mockFeedback);

        mockMvc.perform(post("/api/v1/feedbacks")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .session(mockSession) // Inject mocked session
            .content(objectMapper.writeValueAsString(feedbackDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comment").value("Great suggestion!"))
            .andExpect(jsonPath("$.rating").value(5))
            .andExpect(jsonPath("$.suggestionPackage").value(mockPackage));

        // Verify session attribute was removed
        assertNull(mockSession.getAttribute("suggestionPackage"));
    }
    
    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void testSaveFeedback_Failure_BADREQUEST() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
    

        mockSession.setAttribute("suggestionPackage", null);

        FeedbackDTO feedbackDTO = new FeedbackDTO(5, "Great suggestion!");
        Feedback mockFeedback = new Feedback(feedbackDTO.getComment(), feedbackDTO.getRating(), null);

        when(feedbackService.submitFeedback(any(FeedbackDTO.class), any(SuggestionPackage.class)))
            .thenReturn(mockFeedback);

        mockMvc.perform(post("/api/v1/feedbacks")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .session(mockSession) // Inject mocked session
            .content(objectMapper.writeValueAsString(feedbackDTO)))
            .andExpect(status().isBadRequest());



    }
    

    // ALSO can't get this running with WebMockMVC - security doesn't get implemented for the @PreAuthorize
    // NOTE: reason why it wasn't working before was because our 'ROLE' internally looks like this: "ROLE_USER"
    // we needed to change the Enum to match said request
    @WithMockUser(username = "testUser", roles = {"USER"})
    @Test
    void testGetAllFeedback_Fail() throws Exception {

        when(feedbackService.findAll()).thenReturn(List.of(new Feedback(), new Feedback()));

        mockMvc.perform(get("/api/v1/feedbacks")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
            .andExpect(status().isForbidden())
            .andReturn();
        
        // I KNOW THIS IS SUPPOSE TO BE a 403 error, it just that WEBMVC is not loading the security properly
        // this will be fixed when I do integration testing
    }
    
    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testGetAllFeedback_Correct() throws Exception {

        when(feedbackService.findAll()).thenReturn(List.of(new Feedback(), new Feedback()));

        mockMvc.perform(get("/api/v1/feedbacks")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2)    
        ); // Expect 2 feedback items in array and Return();
    }

    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    @Test
    void testgetIndividualFeedback_Correct() throws Exception {

        Feedback feedback = new Feedback("Great service!", 5 , new SuggestionPackage());
        feedback.setId(1L);

        when(feedbackService.findById(1L)).thenReturn(Optional.of(feedback));
    
        mockMvc.perform(get("/api/v1/feedbacks/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .with(csrf()))
        .andExpect(status().isOk())   
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.comment").value("Great service!"))
        .andExpect(jsonPath("$.rating").value(5));
    
    }


}
