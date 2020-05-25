package com.stocks.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

class TokenProviderTest {

    private TokenProvider tokenProvider;

    @Test
    public void testValidation() {
        tokenProvider = new TokenProvider();

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64
                .decode("fd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8"));

        ReflectionTestUtils.setField(tokenProvider, "key", key);

        String test = Jwts.builder()
                .setSubject("test")
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        Assertions.assertThat(tokenProvider.validateToken(test)).isTrue();
    }

}