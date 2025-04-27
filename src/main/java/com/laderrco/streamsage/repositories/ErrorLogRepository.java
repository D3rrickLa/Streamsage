package com.laderrco.streamsage.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laderrco.streamsage.entities.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long>{
    public List<ErrorLog> findByOrderByTimestampDesc();
}
