package com.pubcrawl.interfaces.user;

public record RegisterUserRequest(
    String username,
    String displayName,
    String password
) {}

