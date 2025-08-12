package com.pubcrawl.interfaces.user;

import com.pubcrawl.application.user.RegisterUserService;
import com.pubcrawl.domain.user.entity.User;

import jakarta.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final RegisterUserService registerUserService;

    public UserController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {
        try {
            User created = registerUserService.register(
                request.username(),
                request.displayName(),
                request.password()
            );

            return ResponseEntity.status(201).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
