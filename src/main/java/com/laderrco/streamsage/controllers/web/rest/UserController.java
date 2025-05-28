package com.laderrco.streamsage.controllers.web.rest;
// for updating things related to the user

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laderrco.streamsage.dtos.AuthenticationRequest;
import com.laderrco.streamsage.dtos.AuthenticationResponse;
import com.laderrco.streamsage.dtos.CredentialsDTO;
import com.laderrco.streamsage.dtos.UserInfoDTO;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.services.TokenService;
import com.laderrco.streamsage.services.Interfaces.UserService;

import jakarta.servlet.http.HttpSession;

import com.laderrco.streamsage.services.UserDeletionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class UserController {
    /*
     * update user's - first/last name, email, password, and delete account
     */

    private final UserService userService;
    private final UserDeletionService userDeletionService;
    private final TokenService tokenService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/profile") // want to seperate out the email and other info, don't need to generate a new token each time your name chagnes
    public ResponseEntity<AuthenticationResponse> updateUserInfo(@RequestBody UserInfoDTO updatedInfoDTO) throws Exception {
        Long userId = userService.findIdByEmail();
        User updatedUser = userService.updateUserProfile(userId, updatedInfoDTO);
        String token = tokenService.generateToken(updatedUser);
        
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/profile")
    public ResponseEntity<UserInfoDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        String userEmail = userDetails.getUsername(); // usually the email

        if (userEmail == null) {        
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Handle missing email in session
        }

        Optional<User> user = userService.findByEmail(userEmail);

        return user.map(value -> ResponseEntity.ok(new UserInfoDTO(value)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // Return 404 instead of exception
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/password") // we might want to update tokens/invalidate them as well
    public ResponseEntity<String> updatePassword(@RequestBody CredentialsDTO credentialsDTO) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.updateUserPassword(userService.findByEmail(authentication.getName()).get().getId(), credentialsDTO);

        return ResponseEntity.ok("Password has been updated successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        /*
         * 1. check if passwords are the same
         * 2. delete the user by Id
         * 3. clear out all tokens -> can't easily be done because of Paseto being statless
         */

        userDeletionService.deleteUserInfo(authenticationRequest);
        
        return ResponseEntity.ok("User has been deleted");
    }

}
