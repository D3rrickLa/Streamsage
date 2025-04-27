package com.laderrco.streamsage.entities;

import com.laderrco.streamsage.domains.Log;

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

    public ErrorLog() {
        super("");
    }

    public ErrorLog(String message, String code, String stacktrace) {
        super(message);
        this.code = code;
        this.stacktrace = stacktrace;
    }
}
