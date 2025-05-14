package com.laderrco.streamsage.Unit.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.laderrco.streamsage.common.exceptions.CacheException;
import com.laderrco.streamsage.configuration.RedisConfig;
import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.services.RedisCacheService;

@ExtendWith(MockitoExtension.class)
@Import(RedisConfig.class)
public class RedisCacheServiceTest {
    
    @Mock
    private RedisTemplate<String, SuggestionPackage> redisTemplate;
    
    @Mock
    private ValueOperations<String, SuggestionPackage> valueOperations;

    private RedisCacheService redisCacheService;

    
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        redisCacheService = new RedisCacheService(redisTemplate);
    }

    @Test
    void testFetchFromRedis_IsNull() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(null);

        SuggestionPackage suggestionPackage = redisCacheService.fetchFromRedis("this is a test prompt");
        assertNull(suggestionPackage);

    }

    @Test
    void testFetchFromRedis_Correct() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(new SuggestionPackage());

        SuggestionPackage suggestionPackage = redisCacheService.fetchFromRedis("this is a test prompt");
        assertNotNull(suggestionPackage);

    }

    @Test
    void testSaveToCache_Correct() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(any(), any(), any());    

        assertDoesNotThrow(() -> 
            redisCacheService.saveToCache("test_prompt", new SuggestionPackage())
        );

        verify(valueOperations).set(anyString(), any(SuggestionPackage.class), any());
    }

    @Test
    void testSave_ThrowException() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doThrow(new CacheException("Redis error", new Throwable())).when(valueOperations).set(any(), any(), any());

        assertThrows(CacheException.class, () -> redisCacheService.saveToCache("test_prompt", new SuggestionPackage()));

        verify(valueOperations).set(any(), any(), any()); // Ensure the method was actually called
    }


}
