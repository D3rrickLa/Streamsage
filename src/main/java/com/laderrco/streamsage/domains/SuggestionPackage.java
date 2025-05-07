package com.laderrco.streamsage.domains;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionPackage {

    private String userPrompt;
    private Long timestamp;

    private List<Recommendation> recommendationList;
}
