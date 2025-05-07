package com.laderrco.streamsage.controllers.web.rest;
// for updating things related to the user

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laderrco.streamsage.dtos.AuthenticationResponse;
import com.laderrco.streamsage.dtos.CredentialsDTO;
import com.laderrco.streamsage.dtos.UserInfoDTO;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.services.TokenService;
import com.laderrco.streamsage.services.Interfaces.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class UserController {
    /*
     * update user's - first/last name, email, password, and delete account
     */

    private final UserService userService;
    private final TokenService tokenService;

    @PutMapping("/profile") // want to seperate out the email and other info, don't need to generate a new token each time your name chagnes
    public ResponseEntity<AuthenticationResponse> updateUserInfo(@RequestBody UserInfoDTO updatedInfoDTO) throws Exception {
        Long userId = userService.findIdByEmail();
        User updatedUser = userService.updateUserProfile(userId, updatedInfoDTO);
        String token = tokenService.generateToken(updatedUser);
        
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);
        return ResponseEntity.ok(authenticationResponse);
    }


    @PutMapping("/password") // we might want to update tokens/invalidate them as well
    public ResponseEntity<String> updatePassword(@RequestBody CredentialsDTO credentialsDTO) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.updateUserPassword(userService.findByEmail(authentication.getName()).get().getId(), credentialsDTO);

        return ResponseEntity.ok("Password has been updated successfully");
    }

}
