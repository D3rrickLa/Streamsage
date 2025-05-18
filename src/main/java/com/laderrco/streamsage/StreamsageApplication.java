package com.laderrco.streamsage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(
	exclude = {
		org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
	}
)
public class StreamsageApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamsageApplication.class, args);
	}

}
