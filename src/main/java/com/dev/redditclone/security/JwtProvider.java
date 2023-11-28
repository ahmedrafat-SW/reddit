package com.dev.redditclone.security;


import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
@Data
public class JwtProvider {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private Instant expireTime;


    public JwtProvider(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public String generateToken(Authentication authentication) {
        Instant issueTime = Instant.now();
        this.setExpireTime(issueTime.plus(15, ChronoUnit.MINUTES));
        JwtClaimsSet claims =  JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(issueTime)
                .expiresAt(expireTime)
                .subject(authentication.getName())
                .claim("scope", "user")
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateTokenWithUser(String username) {
        Instant issueTime = Instant.now();
        expireTime = issueTime.plus(15, ChronoUnit.MINUTES);
        JwtClaimsSet claims =  JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(issueTime)
                .expiresAt(expireTime)
                .subject(username)
                .claim("scope", "user")
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateToken(String token){
        Jwt jwToken = decoder.decode(token);
        Instant expire = jwToken.getExpiresAt();
        assert expire != null;
        return ! Instant.now().isAfter(expire);
    }

    public String getUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }


}
