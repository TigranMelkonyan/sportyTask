package com.sporty.bookstore.config.feigh;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Tigran Melkonyan
 * Date: 4/20/25
 * Time: 12:20 PM
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getCredentials() instanceof final String jwt) {
                requestTemplate.header("Authorization", "Bearer " + jwt);
            }
        };
    }
}
