package com.laderrco.streamsage.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.laderrco.streamsage.repositories")
@Configuration
public class JpaConfig {
    
}
