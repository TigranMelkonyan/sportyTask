package com.sporty.iam.conf.security;

import com.sporty.iam.conf.security.jwt.JwtTokenInspector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenInspector tokenIntrospector;

    public SecurityConfig(
            final AuthenticationEntryPoint jwtAuthenticationEntryPoint,
            final JwtTokenInspector tokenIntrospector) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.tokenIntrospector = tokenIntrospector;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(
                                        "iam/oauth/token",
                                        "/v3/api-docs/**",
                                        "/swagger*/**").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(resourceServer ->
                        resourceServer.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .opaqueToken(token -> token.introspector(tokenIntrospector))
                ).build();
    }

}
