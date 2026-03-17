package com.homebase.ecom.pricing.infrastructure.security;

import com.homebase.ecom.pricing.domain.service.ILockTokenGenerator;

import java.security.SecureRandom;
import java.util.Base64;

public class LockTokenGenerator implements ILockTokenGenerator {
    private static final int TOKEN_LENGTH_BYTES = 32;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateLockToken() {
        byte[] randomBytes = new byte[TOKEN_LENGTH_BYTES];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
