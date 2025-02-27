package com.project1.ms_auth_service.config.auth;

import com.project1.ms_auth_service.config.auth.jwt.JwtService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class TokenAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {
    private static final String BEARER = "Bearer ";

    private static final Predicate<String> matchBearerLength = authValue -> authValue.length() > BEARER.length();

    private static final Function<String, String> isolateBearerValue = authValue -> authValue.substring(BEARER.length());

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
            .map(SecurityUtils::getTokenFromRequest)
            .filter(Objects::nonNull)
            .filter(matchBearerLength)
            .map(isolateBearerValue)
            .filter(token -> !StringUtils.isEmpty(token))
            .map(jwtService::getAuthentication)
            .filter(Objects::nonNull);
    }
}
