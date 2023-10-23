package com.dev.redditclone.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final String AUTHORIZATION_HEADER = "Authorization";
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        if (!Collections.list(request.getHeaderNames()).contains(AUTHORIZATION_HEADER)){
            filterChain.doFilter(request, response);
            return;
        }

        String jwToken = getJwtFromRequest(request);
        boolean isValid = jwtProvider.validateToken(AUTHORIZATION_HEADER);
        if (isValid){
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getWriter().write("Invalid Jwt Token");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return;

    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        String jwt ="";
        if (authHeader != null && StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")){
            jwt = authHeader.substring(7);
        }
        return jwt;
    }


}
