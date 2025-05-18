package com.laderrco.streamsage.Unit.ConfigTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.laderrco.streamsage.configuration.RedisConfig;
import com.laderrco.streamsage.domains.SuggestionPackage;

@ExtendWith(MockitoExtension.class)
public class RedisConfigTest {

    @Mock
    private RedisConnectionFactory connectionFactory;


    private RedisConfig redisConfig;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        redisConfig = new RedisConfig();
    }

    @Test
    void redisTemplate_shouldBeConfiguredCorrectly() {
        // Act
        RedisTemplate<String, SuggestionPackage> redisTemplate = redisConfig.redisTemplate(connectionFactory);

        // Assert basic setup
        assertNotNull(redisTemplate);
        assertEquals(connectionFactory, redisTemplate.getConnectionFactory());

        // Check key serializer
        assertInstanceOf(StringRedisSerializer.class, redisTemplate.getKeySerializer());

        // Check value serializer
        assertInstanceOf(Jackson2JsonRedisSerializer.class, redisTemplate.getValueSerializer());

         // Verify serialization settings
        assertInstanceOf(Jackson2JsonRedisSerializer.class, redisTemplate.getValueSerializer());
    }
}
