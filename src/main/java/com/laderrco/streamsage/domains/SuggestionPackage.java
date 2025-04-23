package com.laderrco.streamsage.domains;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionPackage {

    private String userPrompt;
    private Long timestamp;

    @ElementCollection
    private List<Recommendation> RecommendationList;
}
