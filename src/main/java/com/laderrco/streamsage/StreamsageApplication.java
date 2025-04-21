package com.laderrco.streamsage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
	exclude = {
		org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
		org.springframework.boot.autoconfigure.session.SessionAutoConfiguration.class
	}
)
public class StreamsageApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamsageApplication.class, args);
	}

}
