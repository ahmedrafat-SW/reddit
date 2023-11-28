package com.dev.redditclone.service;


import com.dev.redditclone.dto.AuthenticationResponse;
import com.dev.redditclone.dto.LoginRequest;
import com.dev.redditclone.dto.RefreshTokenRequest;
import com.dev.redditclone.dto.RegisterRequest;
import com.dev.redditclone.exception.SpringRedditException;
import com.dev.redditclone.exception.SubredditNotFoundException;
import com.dev.redditclone.model.NotificationEmail;
import com.dev.redditclone.model.RefreshToken;
import com.dev.redditclone.model.User;
import com.dev.redditclone.model.VerificationToken;
import com.dev.redditclone.repository.UserRepository;
import com.dev.redditclone.repository.VerificationTokenRepository;
import com.dev.redditclone.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final String ACTIVATION_URL = "http://localhost:8080/api/auth/accountVerification/";
    private final String VERIFICATION_MESSAGE =
            "Thank you for signing up to Spring Reddit, please click on the below url to activate your account: ";


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public void register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdDate(Instant.now())
                .isEnabled(false)
                .build();

        this.userRepository.save(user);
        String token = generateVerificationToken(user);
        this.mailService.sendMail(generateActivationMail(user, token));
    }

    private NotificationEmail generateActivationMail(User user, String token) {
        String message = VERIFICATION_MESSAGE + ACTIVATION_URL + token;
        return NotificationEmail.builder()
                .subject("Please Activate your account"+ user.getEmail())
                .body(message)
                .recipient(user.getEmail())
                .build();
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .user(user)
                .expireDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .token(token)
                .build();

        this.verificationTokenRepository.save(verificationToken);
        return token;
    }


    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken =
                this.verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() ->
                new SpringRedditException("Invalid Verification Token."));
        verifyAndEnableAccount(verificationToken.get());
    }

    private void verifyAndEnableAccount(VerificationToken verificationToken) {
        String email = verificationToken.getUser().getEmail();
        User user = this.userRepository.findUserByEmail(email)
                .orElseThrow(() -> new SpringRedditException("Can't find user with email: "+email));
        user.setEnabled(true);
        this.userRepository.save(user);
    }

    public AuthenticationResponse authenticate(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authManager.authenticate(authRequest);
        updateSecurityContextWith(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        return AuthenticationResponse.builder()
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiredAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .username(loginRequest.getUsername())
                .jwToken(jwt).build();
    }

    private void updateSecurityContextWith(Authentication authRequest) {
        SecurityContext jwtCtx = SecurityContextHolder.createEmptyContext();
        jwtCtx.setAuthentication(authRequest);
        SecurityContextHolder.setContext(jwtCtx);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return this.userRepository.findUserByUsername(username)
                .orElseThrow(() -> new SubredditNotFoundException("Can't find user with username: "+username));

    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        this.refreshTokenService.validateToken(refreshTokenRequest.getRefreshToken());
        RefreshToken token = this.refreshTokenService.generateRefreshToken();
        String jwToken = jwtProvider.generateTokenWithUser(refreshTokenRequest.getUsername());

        return  AuthenticationResponse.builder()
                .refreshToken(token.getToken())
                .expiredAt(jwtProvider.getExpireTime())
                .jwToken(jwToken)
                .username(refreshTokenRequest.getUsername())
                .build();
    }

}
