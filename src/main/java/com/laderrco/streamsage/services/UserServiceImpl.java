package com.laderrco.streamsage.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laderrco.streamsage.dtos.AuthenticationRequest;
import com.laderrco.streamsage.dtos.CredentialsDTO;
import com.laderrco.streamsage.dtos.UserInfoDTO;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.repositories.UserRepository;
import com.laderrco.streamsage.services.Interfaces.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;

    @Lazy
    private final PasswordEncoder passwordEncoder;
    
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
    public User updateUserProfile(Long userId, UserInfoDTO requestUser) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEmail(requestUser.getEmail());
        user.setFirstName(requestUser.getFirstName());
        user.setLastName(requestUser.getLastName());
        userRepository.save(user);

        return user;
    }

    @Override
    public void updateUserPassword(Long userId, CredentialsDTO password) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // we don't compare the encrypted passwords directly - hasing algo generate different hashes each time
        if (!passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
            throw new Exception("Incorrect original password");
        } 


        user.setPassword(passwordEncoder.encode(password.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public Long findIdByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getId(); // Use ID instead of email
    }

    @Override
    public void delete(AuthenticationRequest authenticationRequest) throws Exception {
        User user = findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Wrong email address"));
        
        if(!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            throw new Exception("Incorrect password given");
        }

        userRepository.delete(user);
    }    
}
