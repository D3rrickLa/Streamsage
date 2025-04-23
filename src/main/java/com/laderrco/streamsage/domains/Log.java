package com.laderrco.streamsage.domains;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@SuperBuilder
public abstract class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    private String message;
    private Long timestamp;
    public Log(String message) {
       this.message = message;
    }
}
