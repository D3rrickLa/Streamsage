package com.laderrco.streamsage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.laderrco.streamsage.domains.SuggestionPackage;

@Configuration
public class RedisConfig {
    @Bean
    RedisTemplate<String, SuggestionPackage> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, SuggestionPackage> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

        Jackson2JsonRedisSerializer<SuggestionPackage> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, SuggestionPackage.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.afterPropertiesSet();

        return template;
    }
}
