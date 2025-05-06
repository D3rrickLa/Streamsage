package com.laderrco.streamsage.domains;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// protocol for exchanging tokens in app
/**
 * SO Paseto doesn't support claiming, but some Paseto impelmentation do a simulating claim via
 * JSON embedding inside hte token
 * 
 * for our purposes we just encrypt/sign the JSON payload which could have fields (i.e. the thing below)
 * we then manually extract the fields from decrypting
 * 
 * Claims - pieces of data embedded inside hte token to give more info about the session/user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppToken {
    private String userId; // unique id from user table in DB
    private String username;
    private Instant expiresDate;
}
