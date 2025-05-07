package com.laderrco.streamsage.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.repositories.UserRepository;

import lombok.AllArgsConstructor;

// this is used for the app config - avoids the circular error
@AllArgsConstructor
@Service
public class UserLookupService{

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    
}
