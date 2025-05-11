package com.laderrco.streamsage.Unit.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.laderrco.streamsage.services.AIResponseServiceImpl;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AIResponseServiceTest {
    
    @InjectMocks
    private AIResponseServiceImpl aiResponseService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    @Order(1)
    void testSendPrompt_Correct() throws Exception {
        String mockResponse = "Generated response"; // Fix typo in response
        String url = "http://localhost:50001/api/v1/generate";
        Map<String, String> requestBody = Map.of("prompt", "test Prompt");

        // ✅ Mock the RestTemplate call properly
        when(restTemplate.postForObject(eq(url), eq(requestBody), eq(String.class)))
            .thenReturn(mockResponse);

        String response = aiResponseService.sendPrompt("test Prompt");
        System.out.println(response + " test");

        assertEquals("Generated response", response); 
    }
}
