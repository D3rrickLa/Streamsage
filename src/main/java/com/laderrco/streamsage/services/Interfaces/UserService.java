package com.laderrco.streamsage.services.Interfaces;

import java.util.List;
import java.util.Optional;

import com.laderrco.streamsage.entities.User;

public interface UserService {
    public List<User> getUsers();
    public Optional<User> findById(Long id);
    public Optional<User> findByEmail(String email);
    public void updateUserProfile(Long userId, User user) throws Exception;
    public void updateUserPassword(Long userId, String password) throws Exception;
    public User save (User user);
}
