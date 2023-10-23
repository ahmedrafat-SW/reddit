package com.dev.redditclone.controller;

import com.dev.redditclone.dto.AuthenticationResponse;
import com.dev.redditclone.dto.LoginRequest;
import com.dev.redditclone.dto.RegisterRequest;
import com.dev.redditclone.security.JwtProvider;
import com.dev.redditclone.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

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

}
