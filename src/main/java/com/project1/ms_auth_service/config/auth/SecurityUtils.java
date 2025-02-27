package com.project1.ms_auth_service.config.auth;

import com.project1.ms_auth_service.model.entity.User;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityUtils {
    public static String getTokenFromRequest(ServerWebExchange serverWebExchange) {
        String token = serverWebExchange.getRequest()
            .getHeaders()
            .getFirst(HttpHeaders.AUTHORIZATION);
        return StringUtils.isEmpty(token) ? Strings.EMPTY : token;
    }

    public static Mono<String> getUserFromRequest(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getPrincipal()
            .cast(UsernamePasswordAuthenticationToken.class)
            .map(UsernamePasswordAuthenticationToken::getPrincipal)
            .cast(User.class)
            .map(User::getUsername);
    }
}
