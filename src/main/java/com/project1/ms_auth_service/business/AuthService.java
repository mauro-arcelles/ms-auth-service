package com.project1.ms_auth_service.business;

import com.project1.ms_auth_service.model.*;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthResponse> authenticate(Mono<AuthRequest> request);

    Mono<RegisterResponse> register(Mono<RegisterRequest> request);

    Mono<Void> validateToken();
}
