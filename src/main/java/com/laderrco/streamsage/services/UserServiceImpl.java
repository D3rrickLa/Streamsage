package com.laderrco.streamsage.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.repositories.UserRepository;
import com.laderrco.streamsage.services.Interfaces.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateUserProfile(Long userId, User requestUser) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEmail(requestUser.getEmail());
        userRepository.save(user);
    }

    @Override
    public void updateUserPassword(Long userId, String password) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(password);
        userRepository.save(user);
    }

    
}
