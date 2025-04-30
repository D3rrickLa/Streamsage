package com.laderrco.streamsage.domains;

import com.laderrco.streamsage.domains.DTOs.AvailableServiceDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableService {
    public AvailableService(AvailableServiceDTO dto, String type) {
        this.id = dto.getProviderId();
        this.name = dto.getProviderName();
        this.logoURL = "https://image.tmdb.org/t/p/original" + dto.getLogoPath(); // Build proper TMDb logo URL
        this.type = type;
    }
    public AvailableService(String name, String url) {
        this.name = name;
        this.URL = url;
    }
    private Long id;
    private String name;
    private String logoURL;
    private String URL;
    private String type; // rent/buy
}
