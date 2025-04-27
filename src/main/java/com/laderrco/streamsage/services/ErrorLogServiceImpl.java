package com.laderrco.streamsage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.laderrco.streamsage.entities.ErrorLog;
import com.laderrco.streamsage.repositories.ErrorLogRepository;
import com.laderrco.streamsage.services.Interfaces.LogService;
import com.laderrco.streamsage.utils.TimestampGenerator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ErrorLogServiceImpl implements LogService<ErrorLog> {

    private final ErrorLogRepository errorLogRepository;
    private final TimestampGenerator timestampGenerator;


    @Override
    public List<ErrorLog> findAll() {
        return errorLogRepository.findByOrderByTimestampDesc();
    }

    @Override
    public ErrorLog save(ErrorLog log) {
        log.setTimestamp(timestampGenerator.getTimestampUTC());
        return errorLogRepository.save(log);
    }

    
}
