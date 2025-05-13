package com.laderrco.streamsage.domains;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.laderrco.streamsage.domains.Enums.Genre;
import com.laderrco.streamsage.domains.Enums.RecommendationType;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recommendation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private RecommendationType recommendationType;
    private String description;

    private List<Genre> genres;
    private String releaseDate;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "available_service", columnDefinition = "jsonb")
    private List<AvailableService> availableService;
}
