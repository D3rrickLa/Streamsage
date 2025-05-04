package com.laderrco.streamsage.domains;

import java.util.List;

import com.laderrco.streamsage.domains.Enums.Genre;
import com.laderrco.streamsage.domains.Enums.RecommendationType;
import com.laderrco.streamsage.domains.converters.SuggestionPackageAttributeConverter;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recommendation {
    private String title;
    private RecommendationType recommendationType;
    private String description;

    private List<Genre> genres;
    private String releaseDate;

    @Convert(converter = SuggestionPackageAttributeConverter.class)
    private List<AvailableService> availableService;
}
