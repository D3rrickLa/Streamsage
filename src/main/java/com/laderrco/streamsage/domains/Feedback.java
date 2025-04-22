package com.laderrco.streamsage.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Nonnull;
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

    private String recommendationList; // change to acutal class later

    // NOTE: this might be an issue if not lazy in the long run
    @ManyToOne
    @JsonIgnoreProperties({"feedbackList", "password"})
    private User user;

    private Long timestamp;
    

    public Feedback(String comments, Integer rating, String recommendationList) {
        this.comment = comments;
        this.rating = rating;
        this.recommendationList = recommendationList;
    }

}
