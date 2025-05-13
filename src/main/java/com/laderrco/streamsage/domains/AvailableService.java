package com.laderrco.streamsage.domains;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class AvailableService implements Serializable {    
    private static final long serialVersionUID = 1L;


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
