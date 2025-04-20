package com.sporty.iam.conf.security.jwt;

import lombok.Getter;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 8:43 PM
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
