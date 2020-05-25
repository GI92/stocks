package com.stocks.service;

import com.stocks.domain.Authority;
import com.stocks.domain.User;
import com.stocks.repository.AuthorityRepository;
import com.stocks.repository.UserRepository;
import com.stocks.security.Authorities;
import com.stocks.service.dto.UserDTO;
import com.stocks.web.rest.errors.EmailAlreadyExists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final AuthorityRepository authorityRepository;
    private final CacheManager cacheManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AuthorityRepository authorityRepository,
                       CacheManager cacheManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerUser(UserDTO userDTO, String password) {
        userRepository.findByEmail(userDTO.getEmail()).ifPresent(user -> {
            throw new EmailAlreadyExists();
        });

        User user = new User();

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(Authorities.USER).ifPresent(authorities::add);
        user.setAuthorities(authorities);

        userRepository.save(user);
        this.clearUserCaches(user);

        LOG.debug("Created user with email: {}", userDTO.getEmail());

        return userDTO;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USER_BY_LOGIN_CACHE)).evict(user.getEmail());
    }
}
