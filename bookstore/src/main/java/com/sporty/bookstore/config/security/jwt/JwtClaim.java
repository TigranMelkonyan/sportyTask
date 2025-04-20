package com.sporty.bookstore.config.security.jwt;

import lombok.Getter;

/**
 * Created by Tigran Melkonyan
 * Date: 4/20/25
 * Time: 12:28 PM
 */
@Getter
public class JwtClaim {

    private JwtClaim() {
    }

    public static final String EMAIL = "email";
    public static final String SUB = "sub";
    public static final String UID = "uid";
    public static final String EXPIRATION = "exp";
    public static final String AUTHORITIES = "authorities";

}
