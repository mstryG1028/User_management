package com.example.user_management_system.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.user_management_system.dto.AuthResponse;
import com.example.user_management_system.dto.LoginRequest;
import com.example.user_management_system.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {

        // 1. Verify email + password. Wrong credentials throw BadCredentialsException
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // 2. Collect roles, removing the "ROLE_" prefix
        Set<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        // 3. Print the visitor card
        String token = jwtUtil.generateToken(request.getEmail(), roles);

        return new AuthResponse(token);
    }
}