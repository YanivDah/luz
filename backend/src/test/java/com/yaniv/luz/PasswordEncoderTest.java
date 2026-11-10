package com.yaniv.luz;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderTest {

    @Test
    void shouldEncodeAndVerifyPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "LuzTest123!";
        String hash = encoder.encode(password);

        assertTrue(encoder.matches(password, hash));
    }
}