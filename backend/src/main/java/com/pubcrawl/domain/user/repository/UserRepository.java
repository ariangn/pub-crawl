package com.pubcrawl.domain.user.repository;

import com.pubcrawl.domain.user.entity.User;
import com.pubcrawl.domain.user.vo.UserId;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(UserId id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    User save(User user);
}
