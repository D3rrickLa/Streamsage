package com.laderrco.streamsage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.laderrco.streamsage.Unit.ConfigTests.TestConfig;

@Import(TestConfig.class)
@SpringBootTest
@ActiveProfiles("test")
class StreamsageApplicationTests {

	@Test
	void contextLoads() {
	}
}
