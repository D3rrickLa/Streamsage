package com.laderrco.streamsage.services.Interfaces;

import com.laderrco.streamsage.dtos.AuthenticationRequest;
import com.laderrco.streamsage.dtos.AuthenticationResponse;

public interface AuthenticationService {
    public AuthenticationResponse register(AuthenticationRequest request);
    public AuthenticationResponse authenticate(AuthenticationRequest request);
}
