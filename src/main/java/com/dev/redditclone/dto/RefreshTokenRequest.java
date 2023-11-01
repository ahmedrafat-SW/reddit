package com.dev.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenRequest {
    private String username;
    private String refreshToken;
}
