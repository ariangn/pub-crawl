package com.pubcrawl.infrastructure.persistence.user;

import com.pubcrawl.domain.user.entity.User;
import com.pubcrawl.domain.user.repository.UserRepository;
import com.pubcrawl.domain.user.vo.UserId;
import com.pubcrawl.domain.common.vo.String30;
import com.pubcrawl.domain.common.vo.String30NoSpaces;
import com.pubcrawl.domain.common.vo.PasswordHash;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpa;

    public UserRepositoryImpl(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpa.findById(id.getValue()).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpa.findByUsername(username).map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpa.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = toEntity(user);
        UserJpaEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    // mapping
    private User toDomain(UserJpaEntity entity) {
        return new User(
                new UserId(entity.getId()),
                new String30NoSpaces(entity.getUsername()),
                new String30(entity.getDisplayName()),
                new PasswordHash(entity.getPasswordHash())
        );
    }

    private UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId().getValue(),
                user.getUsername().getValue(),
                user.getDisplayName().getValue(),
                user.getPasswordHash().getValue()
        );
    }
}
