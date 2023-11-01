package com.dev.redditclone.controller;

import com.dev.redditclone.dto.AuthenticationResponse;
import com.dev.redditclone.dto.LoginRequest;
import com.dev.redditclone.dto.RefreshTokenRequest;
import com.dev.redditclone.dto.RegisterRequest;
import com.dev.redditclone.service.AuthenticationService;
import com.dev.redditclone.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        this.authenticationService.register(request);
        return ResponseEntity.ok("User Registration Successful");
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        this.authenticationService.verifyAccount(token);
        return ResponseEntity.ok("Account is Activated Successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest){
        AuthenticationResponse response = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("refresh/token")
    public ResponseEntity<AuthenticationResponse> getRefreshToken(@RequestBody RefreshTokenRequest tokenRequest){
        AuthenticationResponse authenticationResponse = this.authenticationService.refreshToken(tokenRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest tokenRequest){
        this.refreshTokenService.deleteToken(tokenRequest.getRefreshToken());
        return ResponseEntity.ok("Refresh Token Successfully deleted.");
    }
}
