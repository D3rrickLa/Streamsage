package com.laderrco.streamsage.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CredentialsDTO {
    private String oldPassword;
    private String newPassword;
}
