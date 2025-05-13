package com.laderrco.streamsage.services;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.laderrco.streamsage.common.exceptions.CacheException;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.utils.QueryNormalizer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedisCacheService {
    private final RedisTemplate<String, SuggestionPackage> redisTemplate;

    public SuggestionPackage fetchFromRedis(String prompt) {
        String normalizedQuery = QueryNormalizer.normalizeQuery(prompt);

        SuggestionPackage cachedRecommendation = (SuggestionPackage) redisTemplate.opsForValue().get(normalizedQuery); 
        if (cachedRecommendation != null) {
            System.out.println("Fetching multiple recommendations from Redis: " + normalizedQuery);
            // cachedRecommendation.setHitCount(cachedRecommendation.getHitCount() + 1); // implement this at a later time
            // cachedRecommendation.setLastAccessed(System.currentTimeMillis());
        
            // save and update cahce 
            redisTemplate.opsForValue().set(normalizedQuery, cachedRecommendation, Duration.ofHours(1L));
            return cachedRecommendation;
        }

        return null;
    }


    public void saveToCache(String prompt, SuggestionPackage suggestionPackage) {
        try {
            redisTemplate.opsForValue().set(QueryNormalizer.normalizeQuery(prompt), suggestionPackage, Duration.ofMinutes(30L));
        } catch (Exception e) {
            throw new CacheException("Error while saving to Redis cache", e);
        }
    }
}
