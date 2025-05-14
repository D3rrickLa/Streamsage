package com.laderrco.streamsage.Unit.ControllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.configuration.PasetoAuthenticationFilter;
import com.laderrco.streamsage.configuration.SecurityConfig;
import com.laderrco.streamsage.controllers.web.rest.ErrorLogController;
import com.laderrco.streamsage.entities.ErrorLog;
import com.laderrco.streamsage.services.Interfaces.LogService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ErrorLogController.class)
@Import(SecurityConfig.class) // Import your custom security config
@AutoConfigureMockMvc
public class ErrorLogControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogService<ErrorLog> logService;

    @MockitoBean
    private PasetoAuthenticationFilter pasetoAuthenticationFilter;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;


    
    // runs before each test method for setup -> override its behaviour and make sure it doesn't interfer with handle response
    @BeforeEach
    void setup() throws ServletException, IOException {
        doAnswer(invocation -> {
            // Simply continue the filter chain without altering the response
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(pasetoAuthenticationFilter).doFilter(any(), any(), any());


        logService.save(new ErrorLog("Test message 1", "001", "example stacktrace"));
        logService.save(new ErrorLog("Test message 2", "002", "example stacktrace"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN") // Try setting roles
    void testGetAllErrorLogs() throws Exception {
        when(logService.findAll()).thenReturn(List.of(
            new ErrorLog("Test message 1", "001", "stacktrace"),
            new ErrorLog("Test message 2", "002", "stacktrace")
        ));
    
        MvcResult result = mockMvc.perform(get("/api/v1/logs/errorlogs")
            .with(csrf()))
            .andExpect(status().isOk())
            .andReturn();
    
    
        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
    
        List<ErrorLog> logs = objectMapper.readValue(jsonResponse, new TypeReference<List<ErrorLog>>(){});
    
        assertEquals(2, logs.size());      
        assertEquals("Test message 1", logs.get(0).getMessage());                
    }
    @Test
    @WithMockUser(username = "testUser", roles = "USER") // Try setting roles
    void testGetAllErrorLogs_WrongRole() throws Exception {
        when(logService.findAll()).thenReturn(List.of(
            new ErrorLog("Test message 1", "001", "stacktrace"),
            new ErrorLog("Test message 2", "002", "stacktrace")
        ));
    
        mockMvc.perform(get("/api/v1/logs/errorlogs")
            .with(csrf()))
            .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN") // Try setting roles
    void testSaveErrorLog_Correct()throws Exception {
        ErrorLog log = new ErrorLog("Test message 1", "001", "example stacktrace");
        when(logService.save(any())).thenReturn(log);
        
        ObjectMapper objectMapper = new ObjectMapper();


        MvcResult result = mockMvc.perform(post("/api/v1/logs/errorlogs")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .content(objectMapper.writeValueAsString(log)))
            .andExpect(status().isOk())
            .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorLog outputLogs = objectMapper.readValue(jsonResponse, new TypeReference<ErrorLog>(){});

        assertEquals(log, outputLogs);

    }
    @Test
    @WithMockUser(username = "testUser", roles = "USER") // Try setting roles
    void testSaveErrorLog_Correct_USERROLE()throws Exception {
        ErrorLog log = new ErrorLog("Test message 1", "001", "example stacktrace");
        when(logService.save(any())).thenReturn(log);
        
        ObjectMapper objectMapper = new ObjectMapper();


        MvcResult result = mockMvc.perform(post("/api/v1/logs/errorlogs")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .content(objectMapper.writeValueAsString(log)))
            .andExpect(status().isOk())
            .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorLog outputLogs = objectMapper.readValue(jsonResponse, new TypeReference<ErrorLog>(){});

        assertEquals(log, outputLogs);

    }

    @Test
    void testSaveErrorLog_NoRole()throws Exception {
        ErrorLog log = new ErrorLog("Test message 1", "001", "example stacktrace");
        when(logService.save(any())).thenReturn(log);
        
        ObjectMapper objectMapper = new ObjectMapper();


        mockMvc.perform(post("/api/v1/logs/errorlogs")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .content(objectMapper.writeValueAsString(log)))
            .andExpect(status().isForbidden());
    }
}
