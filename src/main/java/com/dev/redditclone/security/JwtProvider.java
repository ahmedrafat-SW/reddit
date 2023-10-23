package com.dev.redditclone.security;

import com.dev.redditclone.exception.SpringRedditException;
import com.dev.redditclone.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;


@Service
public class JwtProvider {
    private KeyStore keyStore;

    private final Date EXPRIATION_DATE= new Date(2023 -1900, Calendar.OCTOBER, 25);
    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/reddit-secret.jks");
            keyStore.load(resourceAsStream, "root1234".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringRedditException("Exception occurred while loading keystore");
        }

    }

    public String generateToken(Authentication authentication) {
       User principal =  (User) authentication.getPrincipal();
        return Jwts.builder()
                .setHeader(Map.of("type", "jwt",
                        "kid", UUID.randomUUID().toString()))
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(EXPRIATION_DATE)
                .setAudience("reddit-clone")
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("reddit-secret", "root1234".toCharArray());
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException | NoSuchAlgorithmException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    public boolean validateToken(String jwt) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getPublickey())
                .build().parseClaimsJws(jwt);
        Date expirationDate = claimsJws.getBody().getExpiration();

        return !isExpired(expirationDate);
    }

    private boolean isExpired(Date expirationDate) {
        return new Date().after(expirationDate);
    }

    private PublicKey getPublickey() {
        try {
            return keyStore.getCertificate("reddit-secret").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPublickey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
