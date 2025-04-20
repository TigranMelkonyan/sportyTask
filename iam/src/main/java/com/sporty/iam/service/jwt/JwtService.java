package com.sporty.iam.service.jwt;

import com.sporty.iam.conf.security.jwt.JwtClaim;
import com.sporty.iam.domain.model.user.UserInfoDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 8:52 PM
 */
@Service
@Log4j2
public class JwtService {

    public static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_TYPE_HEADER = "typ";

    private final String jwtSecret;
    private final Duration validity;

    @Autowired
    public JwtService(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.token.validity}") final Duration validity) {
        this.jwtSecret = jwtSecret;
        this.validity = validity;
    }


    public String createJwt(final UserInfoDetails userInfoDetails) {
        log.info("Creating jwt token for user with id - {} ", userInfoDetails.userId());
        final var jti = UUID.randomUUID().toString();
        Date expirationDate = Date.from(Instant.now().plus(validity));
        final Key signingKey = new SecretKeySpec(jwtSecret.getBytes(), algorithm.getJcaName());
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaim.UID, userInfoDetails.userId());
        claims.put(JwtClaim.SUB, userInfoDetails.userId());
        claims.put(JwtClaim.EMAIL, userInfoDetails.email());
        claims.put(JwtClaim.AUTHORITIES, userInfoDetails.role());
        String token = Jwts.builder()
                .setId(jti)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .setHeaderParam(TOKEN_TYPE_HEADER, TOKEN_TYPE)
                .addClaims(claims)
                .signWith(algorithm, signingKey)
                .compact();
        log.info("Successfully created jwt token for user with id - {} ", userInfoDetails.userId());
        return token;
    }

}
