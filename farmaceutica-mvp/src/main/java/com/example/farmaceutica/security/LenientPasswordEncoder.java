package com.example.farmaceutica.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Password encoder that prefers BCrypt hashes but gracefully falls back to
 * plain-text comparison when the stored password is not encoded. This keeps
 * the existing database usable while new credentials can be migrated to
 * BCrypt over time.
 */
public class LenientPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return bcrypt.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }

        String trimmedRaw = rawPassword == null ? "" : rawPassword.toString().trim();
        String trimmedEncoded = encodedPassword.trim();

        if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$")
                || encodedPassword.startsWith("$2y$")) {
            return bcrypt.matches(rawPassword, encodedPassword);
        }

        if (encodedPassword.startsWith("{noop}")) {
            return trimmedEncoded.substring("{noop}".length()).equals(trimmedRaw);
        }

        // Stored password is not a BCrypt hash; compare as plain text (leniently) for legacy data.
        return trimmedEncoded.equals(trimmedRaw);
    }
}