package com.laderrco.streamsage.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.bouncycastle.util.Arrays;
import org.jetbrains.annotations.TestOnly;
import org.paseto4j.commons.PasetoException;
import org.paseto4j.commons.SecretKey;
import org.paseto4j.commons.Version;
import org.paseto4j.version4.Paseto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laderrco.streamsage.domains.AppToken;
import com.laderrco.streamsage.entities.User;

import io.jsonwebtoken.io.Decoders;



@Service
public class TokenService {
    
    @Value("${env.token.paseto.SECRET}")
    private String secret;
    
    @Value("${env.token.paseto.FOOTER}")
    private String footer;


    public String extractUsername(String token) {
        return decrypt(token).map(AppToken::getUsername).orElse(null); // it's the get id issue... why 3 though? -> three because the user we submitted, new one is the third option
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return Date.from(decrypt(token).map(AppToken::getExpiresDate).orElse(null));
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        AppToken appToken = new AppToken(user.getId().toString(), user.getUsername(), now.plus(Duration.ofHours(1L)));

        try{
            String payload = mapper().writeValueAsString(appToken);
            return Paseto.encrypt(key(), payload, footer);
        }
        catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            return "ERROR_GENERATING_TOKEN";
        }
        catch(Exception e) {
            e.printStackTrace();
            return "CRITICAL_ERROR_GENERATING_TOKEN";
        }
    }

    public Optional<String> encrypt(AppToken token) {
        try {
            String payload = mapper().writeValueAsString(token);
            return Optional.of(Paseto.encrypt(key(), payload, footer));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    public Optional<AppToken> decrypt(String token) {
        try {
            String payload = Paseto.decrypt(key(), token, footer);
            AppToken appToken = mapper().readValue(payload, AppToken.class);
            if (Instant.now().isAfter(appToken.getExpiresDate())) {
                return Optional.empty();
            }
            return Optional.of(appToken);
        } catch (PasetoException | JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Key requires a 32 byte length - exact
    // need 2 things, check if greater or less than 32, will tackle the greater for now
    private SecretKey key() {

        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        byte[] trimmedKey = Arrays.copyOf(keyBytes, 32);
        
        return new SecretKey(trimmedKey, Version.V4);
    }

    private JsonMapper mapper() {
        JsonMapper mapper = new JsonMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }


    @TestOnly
    public void testSetAPIandFooter() {
        this.footer = "123456789";
        this.secret = "1234567890";
    }
}
