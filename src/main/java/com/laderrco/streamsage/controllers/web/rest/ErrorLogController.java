package com.laderrco.streamsage.controllers.web.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laderrco.streamsage.entities.ErrorLog;
import com.laderrco.streamsage.services.Interfaces.LogService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/logs")
@AllArgsConstructor
public class ErrorLogController {
    private final LogService<ErrorLog> logService;

    @GetMapping(value = {"errorlogs"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ErrorLog>> getAllErrorLogs() {
        return ResponseEntity.ok(logService.findAll());
    }


    @PostMapping(value = {"errorlogs"})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ErrorLog> saveErrorLog(@RequestBody ErrorLog errorLog) {
        return ResponseEntity.ok(logService.save(errorLog));
    }
}
