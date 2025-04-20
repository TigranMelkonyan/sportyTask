package com.sporty.bookstore.config.security.audit;

import com.sporty.bookstore.config.security.jwt.JwtClaim;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/20/25
 * Time: 2:41 PM
 */
@Component
public class ApplicationAuditorAware {

    public UUID getCurrentAccountId() {
        String uuid = Objects.requireNonNull(getOAuth2Principal()).getAttribute(JwtClaim.UID);
        if (uuid == null)
            return null;
        return UUID.fromString(uuid);
    }

    private OAuth2AuthenticatedPrincipal getOAuth2Principal() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (Objects.isNull(authentication)) {
            return null;
        }
        return (OAuth2AuthenticatedPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
