package com.laderrco.streamsage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

// Tells Spring we want to auth EVERY incomign request, but that request for our auth controller should pass through
//  also will ask Spring to use our prev set up AuthenticationPRovider freom AuthConfig and also to use a Filter we'll create as the last step in this code
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                // .requestMatchers("/api/v1/auth/**").permitAll()
                // .anyRequest().authenticated()
                .anyRequest().permitAll()
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // problem with H2 when connecting with security, need to disable header: https://stackoverflow.com/questions/40165915/why-does-the-h2-console-in-spring-boot-show-a-blank-screen-after-logging-in
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }       
}
