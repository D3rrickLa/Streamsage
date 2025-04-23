package com.laderrco.streamsage.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
public class ErrorLog extends Log {
    private String code;
    
    @Lob
    private String stacktrace;

    public ErrorLog(String message, String code) {
        super(message);
        this.code = code;
    }
}
