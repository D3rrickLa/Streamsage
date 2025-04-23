package com.laderrco.streamsage.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.laderrco.streamsage.domains.SuggestionPackage;

import jakarta.annotation.Nonnull;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

    // https://www.baeldung.com/jpa-embedded-embeddable
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "userPrompt", column = @Column(name = "suggestion_user_prompt")),
        @AttributeOverride(name = "timestamp", column = @Column(name = "suggestion_timestamp")),
    })
    private SuggestionPackage suggestionPackage;

    // NOTE: this might be an issue if not lazy in the long run
    @ManyToOne
    @JsonIgnoreProperties({"feedbackList", "password"})
    private User user;

    private Long timestamp;
    

    public Feedback(String comments, Integer rating, SuggestionPackage suggestionPackage) {
        this.comment = comments;
        this.rating = rating;
        this.suggestionPackage = suggestionPackage;
    }

}
