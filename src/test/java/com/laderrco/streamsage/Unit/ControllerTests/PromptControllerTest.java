package com.laderrco.streamsage.Unit.ControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.laderrco.streamsage.configuration.ApplicationSecurityBypass;
import com.laderrco.streamsage.controllers.web.rest.PromptController;
import com.laderrco.streamsage.domains.Prompt;

@WebMvcTest(PromptController.class)
// @Import(ApplicationSecurityBypass.class) // Ensure your security configuration is applied
public class PromptControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test 
    public void testPostPromptAPI() throws Exception {
        Prompt prompt = new Prompt("Testing");
        mvc.perform(MockMvcRequestBuilders
        .post("/api/v1/prompts")
        .content(objectMapper.writeValueAsString(prompt))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.userPrompt").value("Testing"));
    }
}
