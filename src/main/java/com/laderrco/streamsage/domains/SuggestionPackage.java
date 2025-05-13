package com.laderrco.streamsage.domains;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("suggestions")
public class SuggestionPackage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userPrompt;
    private Long timestamp;
    private List<Recommendation> recommendationList;
}
