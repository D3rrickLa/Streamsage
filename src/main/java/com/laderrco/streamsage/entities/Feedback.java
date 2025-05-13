package com.laderrco.streamsage.entities;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.laderrco.streamsage.domains.SuggestionPackage;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity // No specially restriction on the properties
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nonnull
    private Integer rating;


    @Lob
    private String comment;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private SuggestionPackage suggestionPackage;

    @ManyToOne
    @JsonIgnoreProperties({"password"})
    @OnDelete(action = OnDeleteAction.CASCADE) // Automatically delete feedback when user is deleted
    private User user;

    private Long timestamp;

    public Feedback(String comments, Integer rating, SuggestionPackage suggestionPackage) {
        this.comment = comments;
        this.rating = rating;
        this.suggestionPackage = suggestionPackage;
    }

}
