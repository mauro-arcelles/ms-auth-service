package com.project1.ms_auth_service.config.auth.jwt;

import com.project1.ms_auth_service.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
public class JWTReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<Authentication> authenticate(final Authentication authentication) {
        if (authentication.isAuthenticated()) {
            return Mono.just(authentication);
        }
        return Mono.just(authentication)
            .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
            .cast(UsernamePasswordAuthenticationToken.class)
            .flatMap(this::authenticateToken)
            .publishOn(Schedulers.parallel())
            .onErrorResume(e -> raiseBadCredentials())
            .filter(u -> passwordEncoder.matches((String) authentication.getCredentials(), u.getPassword()))
            .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
            .map(u -> new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), u.getAuthorities()));
    }

    private <T> Mono<T> raiseBadCredentials() {
        return Mono.error(new BadRequestException("Invalid Credentials"));
    }

    private Mono<UserDetails> authenticateToken(final UsernamePasswordAuthenticationToken authenticationToken) {
        String username = authenticationToken.getName();

        log.info("checking authentication for user {}", username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("authenticated user {}, setting security context", username);
            return this.userDetailsService.findByUsername(username);
        }

        return null;
    }
}
