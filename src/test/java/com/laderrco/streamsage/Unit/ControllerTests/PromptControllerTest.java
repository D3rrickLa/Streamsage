package com.laderrco.streamsage.Unit.ControllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.laderrco.streamsage.configuration.PasetoAuthenticationFilter;
import com.laderrco.streamsage.controllers.web.rest.PromptController;
import com.laderrco.streamsage.domains.Prompt;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.services.Interfaces.AIResponseService;
import com.laderrco.streamsage.services.Interfaces.RecommendationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.io.IOException;

// testing the method only, do not interact with the DB - mock the service layer and test only controller logic
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PromptController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = "USER") // Try setting roles
public class PromptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AIResponseService aiResponseService;

    @MockitoBean
    private RecommendationService recommendationService;

    @MockitoBean
    private PasetoAuthenticationFilter pasetoAuthenticationFilter;




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


    @Test
    void testSendPrompt_ReturnsSuggestionPackage() throws Exception {
        when(aiResponseService.sendPrompt(anyString())).thenReturn("mock response");
        when(recommendationService.returnSuggestionPackage(any(), any())).thenReturn(new SuggestionPackage());

        MvcResult result = mockMvc.perform(post("/api/v1/prompts/")
            .header("Accept-Language", "en-CA")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()) // need csrf allowed -> WebMVCTest loads only the web layer and not the whole context: https://stackoverflow.com/questions/52994063/spring-webmvctest-with-post-returns-403
            .content("{ \"prompt\": \"Test prompt\" }"))
            .andExpect(status().isCreated())
            .andReturn(); 

            // Print response body for debugging
        System.out.println("Response: " + result.getResponse().getContentAsString());

    }

    @Test
    void testSendPrompt_ReturnsFilledSUggestionPackage() throws Exception {
        SuggestionPackage suggestionPackage = new SuggestionPackage();
        suggestionPackage.setUserPrompt("Testing User Prompt");
        suggestionPackage.setTimestamp(1234567890L);
        
        when(aiResponseService.sendPrompt(anyString())).thenReturn("mock AI response");
        when(recommendationService.returnSuggestionPackage(any(), any())).thenReturn(suggestionPackage);

        mockMvc.perform(post("/api/v1/prompts/")
        .header("Accept-Language", "en-CA")
        .contentType(MediaType.APPLICATION_JSON)
        .with(csrf()) // need csrf allowed -> WebMVCTest loads only the web layer and not the whole context: https://stackoverflow.com/questions/52994063/spring-webmvctest-with-post-returns-403
        .content("{ \"prompt\": \"Testing User Prompt\" }"))
        .andExpect(status().isCreated())
        .andExpect(header().string("Content-Type", "application/json"))
        .andExpect(jsonPath("$.userPrompt").value("Testing User Prompt"))
        .andExpect(jsonPath("$.timestamp").value(1234567890));

    }

    @Test
    void testSendPrompt_InvalidHeader() throws Exception {
        when(aiResponseService.sendPrompt(anyString())).thenReturn("mock response");
        when(recommendationService.returnSuggestionPackage(any(), any())).thenReturn(new SuggestionPackage());

        mockMvc.perform(post("/api/v1/prompts/")
            .header("Accept-Language", "en-CA")
            .contentType(MediaType.APPLICATION_XML)
            .with(csrf()) // need csrf allowed -> WebMVCTest loads only the web layer and not the whole context: https://stackoverflow.com/questions/52994063/spring-webmvctest-with-post-returns-403
            .content("{ \"prompt\": \"Test prompt\" }"))
            .andExpect(status().isUnsupportedMediaType());
    }


    @Test
    void testSendPrompt_SessionAttributeSet() throws Exception {
        SuggestionPackage suggestionPackage = new SuggestionPackage();
        suggestionPackage.setUserPrompt("Testing User Prompt");
        suggestionPackage.setTimestamp(1234567890L);
        
        when(aiResponseService.sendPrompt(anyString())).thenReturn("mock AI response");
        when(recommendationService.returnSuggestionPackage(any(), any())).thenReturn(suggestionPackage);

        MvcResult result = mockMvc.perform(post("/api/v1/prompts/")
        .header("Accept-Language", "en-CA")
        .contentType(MediaType.APPLICATION_JSON)
        .with(csrf())
        .content("{ \"prompt\": \"Testing User Prompt\" }"))
        .andReturn();

        HttpSession session = result.getRequest().getSession();
        assertNotNull(session.getAttribute("suggestionPackage")); // Verify session stores data
    }

    // acutal unit test demo
    @Test
    void testSendPrompt() throws Exception {
        Prompt prompt = new Prompt("Test prompt");

        SuggestionPackage suggestionPackage = new SuggestionPackage();
        suggestionPackage.setUserPrompt("Testing User Prompt");
        suggestionPackage.setTimestamp(1234567890L);

        when(aiResponseService.sendPrompt(anyString())).thenReturn("mock AI response");
        when(recommendationService.returnSuggestionPackage(any(), any())).thenReturn(suggestionPackage);

        PromptController controller = new PromptController(aiResponseService, recommendationService);
        HttpSession mockSession = mock(HttpSession.class);

        ResponseEntity<SuggestionPackage> response = controller.sendPrompt(mockSession, prompt, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Testing User Prompt", response.getBody().getUserPrompt());
    }
}
