package com.laderrco.streamsage.domains;

import java.util.List;

import com.laderrco.streamsage.domains.Enums.RecommendationType;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {
    private String title;
    private RecommendationType recommendationType;
    private String description;

    // @ElementCollection
    // private List<AvailableService> availableService;
}
