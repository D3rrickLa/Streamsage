// package com.laderrco.streamsage.configuration;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.web.SecurityFilterChain;

// // @Configuration
// public class ApplicationSecurityBypass {
//     // @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//         .csrf(AbstractHttpConfigurer::disable)
//         .headers((header) -> header.frameOptions((frameOptions) -> frameOptions.disable()));
//         return http.build();
//     }
// }
