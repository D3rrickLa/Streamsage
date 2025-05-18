package com.laderrco.streamsage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.laderrco.streamsage.Unit.ConfigTests.TestConfig;

@Import(TestConfig.class)
@SpringBootTest
class StreamsageApplicationTests {

	@Test
	void contextLoads() {
	}
}
