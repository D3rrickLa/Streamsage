package com.laderrco.streamsage.controllers.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laderrco.streamsage.entities.AuthenticationRequest;
import com.laderrco.streamsage.entities.AuthenticationResponse;
import com.laderrco.streamsage.services.AuthenticationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    // map incoming POST requests to register a new user
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    // map incoming POST requests to authenticate an existing user
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
