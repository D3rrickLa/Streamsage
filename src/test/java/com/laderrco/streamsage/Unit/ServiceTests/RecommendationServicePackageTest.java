package com.laderrco.streamsage.Unit.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.domains.AvailableService;
import com.laderrco.streamsage.domains.Prompt;
import com.laderrco.streamsage.domains.Recommendation;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.domains.Enums.Genre;
import com.laderrco.streamsage.dtos.MovieInfoDTO;
import com.laderrco.streamsage.services.RecommendationServiceImpl;
import com.laderrco.streamsage.services.Interfaces.MediaLookupService;
import com.laderrco.streamsage.utils.TimestampGenerator;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecommendationServicePackageTest {
    
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private MediaLookupService mediaLookupService;

    @Mock
    private TimestampGenerator timestampGenerator;

    private RecommendationServiceImpl recommendationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        recommendationService = new RecommendationServiceImpl(objectMapper, mediaLookupService, timestampGenerator);
    }


    @Test
    void testReturnSuggestionPackage_Correct() throws Exception {
        // Mock AI response
        String aiResponse = "{ \"message\": [\"Test Movie\"] }";
        ResponseEntity<String> mockResponse = ResponseEntity.ok("{\"mockKey\": \"mockValue\"}");

        when(mediaLookupService.apiResponse(anyString())).thenReturn(mockResponse);

        // Mock MovieInfoDTO
        MovieInfoDTO movieInfoDTO = new MovieInfoDTO();
        movieInfoDTO.setOriginalTitle("Test Movie");
        movieInfoDTO.setOverview("Test Description");
        movieInfoDTO.setGenres(List.of(Genre.ACTION.getId())); // Assuming 1 represents Genre.ACTION
        movieInfoDTO.setReleaseDate("2002-02-02");

        when(mediaLookupService.getBestMatchDto(eq(mockResponse.getBody()), anyString())).thenReturn(movieInfoDTO);
        when(mediaLookupService.getListOfServices(any())).thenReturn(List.of(new AvailableService(1L, "Netflix", "string/url", "rent/buy")));

        // Mock Prompt
        Prompt prompt = new Prompt("some prompt");

        // Invoke method
        SuggestionPackage suggestionPackage = recommendationService.returnSuggestionPackage(prompt, aiResponse);

        // Assertions
        assertNotNull(suggestionPackage);
        assertEquals(prompt.getPrompt(), suggestionPackage.getUserPrompt());
        assertFalse(suggestionPackage.getRecommendationList().isEmpty());

        Recommendation recommendation = suggestionPackage.getRecommendationList().get(0);
        assertEquals("Test Movie", recommendation.getTitle());
        assertEquals("Test Description", recommendation.getDescription());
        assertEquals(List.of(Genre.ACTION), recommendation.getGenres());
        assertEquals("2002-02-02", recommendation.getReleaseDate());
        assertEquals(List.of(new AvailableService(1L, "Netflix", "string/url", "rent/buy")), recommendation.getAvailableService());
    }
}
