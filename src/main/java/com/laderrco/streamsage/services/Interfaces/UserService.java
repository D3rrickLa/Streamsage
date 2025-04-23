package com.laderrco.streamsage.services.Interfaces;

import java.util.List;
import java.util.Optional;

import com.laderrco.streamsage.entities.User;

public interface UserService {
    public List<User> findAll();
    public Optional<User> findById(Long id);
    public Optional<User> findByEmail(String email);
    public User save (User user);
}
