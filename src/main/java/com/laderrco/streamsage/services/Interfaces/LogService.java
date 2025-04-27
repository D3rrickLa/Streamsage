package com.laderrco.streamsage.services.Interfaces;

import java.util.List;

import com.laderrco.streamsage.domains.Log;

public interface LogService<T extends Log> {
    public List<T> findAll();
    public T save(T log);
}
