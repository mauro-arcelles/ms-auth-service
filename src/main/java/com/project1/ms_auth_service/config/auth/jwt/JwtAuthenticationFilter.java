package com.project1.ms_auth_service.config.auth.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ReactiveUserDetailsService reactiveUserDetailsService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest()
            .getHeaders()
            .getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);

        if (username != null) {
            return reactiveUserDetailsService.findByUsername(username)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()))
                .map(ReactiveSecurityContextHolder::withAuthentication)
                .then(chain.filter(exchange));
        }

        return chain.filter(exchange);
    }
}
