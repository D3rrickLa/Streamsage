package com.laderrco.streamsage.utils;

import java.time.Instant;

import org.springframework.stereotype.Component;

// https://stackoverflow.com/questions/26022361/instantiating-util-class-in-spring
// https://stackoverflow.com/questions/13746080/spring-or-not-spring-should-we-create-a-component-on-a-class-with-static-meth
// https://www.baeldung.com/spring-component-annotation
@Component
public final class TimestampGenerator {
    public Long getTimestampUTC() {
        return Instant.now().toEpochMilli();
    }
}
