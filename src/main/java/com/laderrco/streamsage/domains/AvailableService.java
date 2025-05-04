package com.laderrco.streamsage.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AvailableService {

    @NonNull
    private Long id; // id from tmdb 

    @NonNull
    private String name;
    
    @NonNull
    private String logoURL;
    
    private String URL; // this might be a pipe dream, no way for the API to get the link info
    
    @NonNull
    private String type; // rent/buy
}
