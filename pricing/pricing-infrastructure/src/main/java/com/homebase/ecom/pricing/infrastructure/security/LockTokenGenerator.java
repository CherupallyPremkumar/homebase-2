package com.homebase.ecom.pricing.infrastructure.security;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class LockTokenGenerator {
    private static final int TOKEN_LENGTH_BYTES = 32;
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateLockToken() {
        byte[] randomBytes = new byte[TOKEN_LENGTH_BYTES];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
