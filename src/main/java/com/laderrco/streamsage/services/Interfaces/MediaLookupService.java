package com.laderrco.streamsage.services.Interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.laderrco.streamsage.domains.AvailableService;
import com.laderrco.streamsage.dtos.MovieInfoDTO;

public interface MediaLookupService {
    public ResponseEntity<String> apiResponse(String mediaName);
    public MovieInfoDTO getBestMatchDto(String jsonResponse, String mediaName);
    public List<AvailableService> getListOfServices(Long id);
}
