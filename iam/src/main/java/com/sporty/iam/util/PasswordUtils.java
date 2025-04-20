package com.sporty.iam.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * Created by Tigran Melkonyan
 * Date: 2/19/25
 * Time: 2:01 PM
 */
public final class PasswordUtils {

    private PasswordUtils() {
    }

    static BCryptPasswordEncoder bCryptPasswordEncoder =
            new BCryptPasswordEncoder(10, new SecureRandom());

    public static String encode(String password) {
        return bCryptPasswordEncoder.encode(password);

    }

    public static boolean isPasswordMatch(String password, String encodedPassword) {
        return bCryptPasswordEncoder.matches(password, encodedPassword);

    }
}
