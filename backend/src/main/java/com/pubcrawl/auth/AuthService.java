package com.pubcrawl.auth;

import com.pubcrawl.user.User;
import com.pubcrawl.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        var email = extractEmailFromUserDetails(userDetails);

        return userRepository.findByEmail(email).orElse(null);
    }

    private String extractEmailFromUserDetails(org.springframework.security.core.userdetails.UserDetails userDetails) {
        // Spring Security UserDetails.getUsername() returns the email in our case
        return userDetails.getUsername();
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    public Jwt refreshAccessToken(String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        var user = userRepository.findById(UUID.fromString(jwt.getUserId())).orElseThrow();
        return jwtService.generateAccessToken(user);
    }
}
