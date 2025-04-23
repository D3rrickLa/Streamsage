package com.laderrco.streamsage.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laderrco.streamsage.domains.AuthenticationRequest;
import com.laderrco.streamsage.domains.AuthenticationResponse;
import com.laderrco.streamsage.domains.Enums.Roles;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.services.Interfaces.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    // method to register a new user into our DB + generate a JWT for them
    public AuthenticationResponse register(AuthenticationRequest request) {
        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Roles.USER)
            .build();
       
        userService.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    } 
    
    // a method to authenticate an existing user and generate a JWT for them
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userService.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user); 
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
