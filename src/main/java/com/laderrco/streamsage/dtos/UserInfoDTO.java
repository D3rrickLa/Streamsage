package com.laderrco.streamsage.dtos;

import com.laderrco.streamsage.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    public UserInfoDTO(User updatedUser) {
        this.email = updatedUser.getUsername();
        this.firstName = updatedUser.getFirstName();
        this.lastName = updatedUser.getLastName();
    }
    private String email;
    private String firstName;
    private String lastName;
}
