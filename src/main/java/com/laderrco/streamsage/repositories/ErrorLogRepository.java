package com.laderrco.streamsage.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.laderrco.streamsage.entities.ErrorLog;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long>{
    public List<ErrorLog> findByOrderByTimestampDesc();
}
