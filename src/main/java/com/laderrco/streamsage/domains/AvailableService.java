package com.laderrco.streamsage.domains;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class AvailableService {
    private String name;
    private String URL;
}
