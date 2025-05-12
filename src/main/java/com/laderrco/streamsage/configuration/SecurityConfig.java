package com.laderrco.streamsage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true, 
    securedEnabled = true, 
    jsr250Enabled = true) // needed for @PreAuthorize Annotation
public class SecurityConfig {
    
    private final PasetoAuthenticationFilter pasetoAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(crsf -> crsf.disable())
            .headers((header) -> header.frameOptions((frameOptions) -> frameOptions.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/feedbacks").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/prompts").permitAll()
                .anyRequest().authenticated()
            )
            
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(pasetoAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
