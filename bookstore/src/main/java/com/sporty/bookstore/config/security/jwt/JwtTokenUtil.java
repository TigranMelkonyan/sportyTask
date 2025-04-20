package com.sporty.bookstore.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Tigran Melkonyan
 * Date: 4/20/25
 * Time: 12:27 PM
 */
@Component
public class JwtTokenUtil {

    private final String secret;

    public JwtTokenUtil(
            @Value("${jwt.secret}") final String secret
    ) {
        this.secret = secret;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, JwtClaim.SUB, String.class);
    }

    public Instant getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, JwtClaim.EXPIRATION, Date.class).toInstant();
    }

    public <T> T getClaimFromToken(String token, String claim, Class<T> claz) {
        final Claims claims = getAllClaimsFromToken(token);
        if (claims.containsKey(claim)) {
            return claims.get(claim, claz);
        }
        return null;
    }

    public Claims getAllClaimsFromToken(String token) {
        final Key signingKey = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Instant expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(Instant.now());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        checkJwtClaims(token);
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public void validateToken(String token) {
        checkJwtClaims(token);
        if (isTokenExpired(token)) {
            throw new IllegalArgumentException("JWT token is expired");
        }
    }

    public Collection<GrantedAuthority> extractGrantedAuthorities(String token) {
        String authoritiesString = getClaimFromToken(token, JwtClaim.AUTHORITIES, String.class);
        if (authoritiesString == null || authoritiesString.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(authoritiesString.split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private void checkJwtClaims(String token) {
        List<String> requiredClaims = Arrays.asList(
                JwtClaim.UID,
                JwtClaim.SUB,
                JwtClaim.EMAIL,
                JwtClaim.EXPIRATION
        );
        Claims claims = getAllClaimsFromToken(token);
        requiredClaims.forEach(claim -> {
            if (!claims.containsKey(claim)) {
                throw new IllegalArgumentException("JWT token doesn't contain all required claims");
            }
        });
    }
}
