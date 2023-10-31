package com.dev.redditclone.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class JwtProvider {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public JwtProvider(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public String generateToken(Authentication authentication) {
        Instant issueTime = Instant.now();
        Instant expireTime = issueTime.plus(5, ChronoUnit.DAYS);
        JwtClaimsSet claims =  JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(issueTime)
                .expiresAt(expireTime)
                .subject(authentication.getName())
                .claim("scope", "user")
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateToken(String token){
        Jwt jwToken = decoder.decode(token);
        Instant expire = jwToken.getExpiresAt();
        assert expire != null;
        return expire.isBefore(Instant.now());
    }

    public String getUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

}
