package com.laderrco.streamsage.services.Interfaces;

import org.springframework.http.ResponseEntity;

public interface MediaLookupService {
    public ResponseEntity<String> apiResponse(String mediaName);
}
