package com.laderrco.streamsage.Unit.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import com.laderrco.streamsage.domains.AppToken;
import com.laderrco.streamsage.domains.Enums.Roles;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.services.TokenService;



public class TokenServiceTest {
    
    private TokenService tokenService;

    private User user;

    @BeforeEach
    void init() {
        tokenService = new TokenService();
        MockitoAnnotations.openMocks(this);

        user = User.builder()
            .id(1L)
            .email("john@example.com")
            .password("password")
            .role(Roles.ROLE_USER)
            .build();
    }


    @Test
    void testGenerateToken_Correct() {
        tokenService.testSetAPIandFooter();
        String token = tokenService.generateToken(user);
        
        assertNotNull(token);
    }

    @Test
    void testGenerateToken_Error() {
        String token = tokenService.generateToken(user);
        
        assertEquals(token,  "CRITICAL_ERROR_GENERATING_TOKEN");
    }

    @Test
    void testExtractUsername_Correct() {
        tokenService.testSetAPIandFooter();
        String token = tokenService.generateToken(user);

        String usernameTest = tokenService.extractUsername(token);

        assertEquals(user.getUsername(), usernameTest);

    }


    @Test
    void testEncrypt_Correct() {
        AppToken appTokenTest = new AppToken();
        appTokenTest.setExpiresDate(Instant.now());
        appTokenTest.setUserId("1L");
        appTokenTest.setUsername("john@example.com");

        Optional<String> token = tokenService.encrypt(appTokenTest);

        assertNotNull(token);

    }

}
