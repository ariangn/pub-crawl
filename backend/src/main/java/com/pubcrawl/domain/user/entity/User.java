package com.pubcrawl.domain.user.entity;

import com.pubcrawl.domain.common.vo.String30;
import com.pubcrawl.domain.common.vo.String30NoSpaces;
import com.pubcrawl.domain.common.vo.PasswordHash;
import com.pubcrawl.domain.user.vo.UserId;

import java.util.Objects;

public class User {

    private final UserId id;
    private final String30NoSpaces username;
    private final String30 displayName;
    private final PasswordHash passwordHash;

    public User(UserId id, String30NoSpaces username, String30 displayName, PasswordHash passwordHash) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.passwordHash = passwordHash;
    }

    public UserId getId() {
        return id;
    }

    public String30NoSpaces getUsername() {
        return username;
    }

    public String30 getDisplayName() {
        return displayName;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
