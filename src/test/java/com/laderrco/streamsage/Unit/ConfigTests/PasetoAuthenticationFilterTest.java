package com.laderrco.streamsage.Unit.ConfigTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.laderrco.streamsage.configuration.PasetoAuthenticationFilter;
import com.laderrco.streamsage.domains.AppToken;
import com.laderrco.streamsage.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class PasetoAuthenticationFilterTest {
    
    @Mock
    private TokenService tokenService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private PasetoAuthenticationFilter filter;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        filter = new PasetoAuthenticationFilter(tokenService, userDetailsService);

    }


    @Test
    void testDoFilterInternal_WithNoAutHeader() throws Exception{
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WithInvalidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer badtoken");

        filter.doFilter(request, response, filterChain);
        
        // Assert: either no continuation or proper error handling
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws Exception {
        String email = "test@example.com";
        AppToken appToken = new AppToken("123", email, Instant.now().plusSeconds(3600));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            appToken.getUsername(), "password", List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // had to mock these things in the filter for it to work
        when(request.getHeader("Authorization")).thenReturn("Bearer token-test");
        when(tokenService.decrypt(any())).thenReturn(Optional.of(appToken));
        when(tokenService.extractUsername(any())).thenReturn(appToken.getUsername());
        when(userDetailsService.loadUserByUsername(appToken.getUsername())).thenReturn(userDetails);
        when(tokenService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);


        filter.doFilter(request, response, filterChain);
        
        verify(filterChain).doFilter(request, response);

    }
}
