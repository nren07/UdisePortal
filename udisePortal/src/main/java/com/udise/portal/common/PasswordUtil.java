package com.udise.portal.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordUtil {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Method to hash the password
    public static String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    // Method to verify the password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}
