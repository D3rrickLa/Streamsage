package com.laderrco.streamsage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.laderrco.streamsage.Unit.ConfigTests.TestConfig;

@Testcontainers
@Import(TestConfig.class)
@SpringBootTest
@ActiveProfiles("test")
class StreamsageApplicationTests {

	@Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1");
	
	@DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
	
	@Test
	void contextLoads() {
	}
}
