package com.pubcrawl.application.user;

import com.pubcrawl.domain.common.vo.PasswordHash;
import com.pubcrawl.domain.common.vo.String30;
import com.pubcrawl.domain.common.vo.String30NoSpaces;
import com.pubcrawl.domain.user.entity.User;
import com.pubcrawl.domain.user.repository.UserRepository;
import com.pubcrawl.domain.user.vo.UserId;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegisterUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String rawUsername, String rawDisplayName, String rawPassword) {
        // validate username uniqueness
        if (userRepository.existsByUsername(rawUsername)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // hash password
        String hashed = passwordEncoder.encode(rawPassword);

        // create User entity
        User user = new User(
            new UserId(UUID.randomUUID()),
            new String30NoSpaces(rawUsername),
            new String30(rawDisplayName),
            new PasswordHash(hashed)
        );

        // save user
        return userRepository.save(user);
    }
}
