package com.project1.ms_auth_service;

import com.project1.ms_auth_service.api.AuthApiDelegate;
import com.project1.ms_auth_service.business.AuthService;
import com.project1.ms_auth_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthApiDelegateImpl implements AuthApiDelegate {

    @Autowired
    private AuthService authService;

    @Override
    public Mono<ResponseEntity<AuthResponse>> login(Mono<AuthRequest> authRequest, ServerWebExchange exchange) {
        return authService.authenticate(authRequest).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<RegisterResponse>> register(Mono<RegisterRequest> registerRequest, ServerWebExchange exchange) {
        return authService.register(registerRequest).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> validateToken(ServerWebExchange exchange) {
        return authService.validateToken().map(ResponseEntity::ok);
    }
}
