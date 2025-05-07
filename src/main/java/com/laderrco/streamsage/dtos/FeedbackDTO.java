package com.laderrco.streamsage.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackDTO {
    private Integer rating;
    private String comment;
}
