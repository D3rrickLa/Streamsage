package com.laderrco.streamsage.services.Interfaces;

import org.springframework.http.ResponseEntity;

import com.laderrco.streamsage.domains.DTOs.MovieInfoDTO;

public interface MediaLookupService {
    public ResponseEntity<String> apiResponse(String mediaName);
    public MovieInfoDTO getBestMatchDto(String jsonResponse, String mediaName);
}
