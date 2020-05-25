package com.stocks.repository;

import com.stocks.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USER_BY_LOGIN_CACHE = "userByLogin";

    @Cacheable(cacheNames = USER_BY_LOGIN_CACHE)
    Optional<User> findByEmail(String email);
}
