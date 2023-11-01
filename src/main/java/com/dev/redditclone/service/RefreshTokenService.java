package com.dev.redditclone.service;

import com.dev.redditclone.exception.SpringRedditException;
import com.dev.redditclone.model.RefreshToken;
import com.dev.redditclone.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .build();
        return this.refreshTokenRepository.save(refreshToken);
    }
    public void validateToken(String token){
        this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid Refresh Token."));
    }

    public void deleteToken(String token){
        this.refreshTokenRepository.deleteByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid Refresh Token."));
    }

}
