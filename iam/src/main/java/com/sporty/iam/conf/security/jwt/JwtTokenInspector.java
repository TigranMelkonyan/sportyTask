package com.sporty.iam.conf.security.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tigran Melkonyan
 * Date: 4/19/25
 * Time: 8:40 PM
 */
@Component
public class JwtTokenInspector implements OpaqueTokenIntrospector {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtTokenInspector(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        try {
            jwtTokenUtil.validateToken(token);
            return new DefaultOAuth2AuthenticatedPrincipal(
                    jwtTokenUtil.getUsernameFromToken(token),
                    getAttributes(token),
                    jwtTokenUtil.extractGrantedAuthorities(token));
        } catch (JwtException e) {
            throw new OAuth2IntrospectionException("Invalid JWT token", e);
        }
    }

    private Map<String, Object> getAttributes(String token) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(JwtClaim.SUB, jwtTokenUtil.getClaimFromToken(token, JwtClaim.SUB, String.class));
        attributes.put(JwtClaim.EXPIRATION, jwtTokenUtil.getExpirationDateFromToken(token));
        attributes.put(JwtClaim.UID, jwtTokenUtil.getClaimFromToken(token, JwtClaim.UID, String.class));
        attributes.put(JwtClaim.EMAIL, jwtTokenUtil.getClaimFromToken(token, JwtClaim.EMAIL, String.class));
        return attributes;
    }
}
