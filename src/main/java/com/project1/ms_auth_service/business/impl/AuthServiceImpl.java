package com.project1.ms_auth_service.business.impl;

import com.project1.ms_auth_service.business.AuthService;
import com.project1.ms_auth_service.config.auth.jwt.JwtService;
import com.project1.ms_auth_service.business.mapper.AuthMapper;
import com.project1.ms_auth_service.exception.BadRequestException;
import com.project1.ms_auth_service.model.*;
import com.project1.ms_auth_service.model.entity.Role;
import com.project1.ms_auth_service.model.entity.RoleEnum;
import com.project1.ms_auth_service.model.entity.User;
import com.project1.ms_auth_service.repository.RoleRepository;
import com.project1.ms_auth_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    public Mono<AuthResponse> authenticate(Mono<AuthRequest> request) {
        return request
            .flatMap(req -> authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    req.getUsername(),
                    req.getPassword()
                )
            ))
            .map(auth -> {
                SecurityContextHolder.getContext().setAuthentication(auth);
                return authMapper.getAuthResponse(jwtService.generateToken(auth));
            });
    }

    public Mono<RegisterResponse> register(Mono<RegisterRequest> request) {
        return request.flatMap(req ->
                userRepository.existsByUsername(req.getUsername())
                    .flatMap(exists -> {
                        if (exists) {
                            throw new BadRequestException("Username already exists");
                        }
                        return Mono.just(req);
                    }))
            .flatMap(req ->
                roleRepository.findByName(RoleEnum.ROLE_USER.toString())
                    .map(role -> {
                        User user = authMapper.getRegisterUserEntity(req);
                        user.setPassword(passwordEncoder.encode(req.getPassword()));
                        Set<Role> roles = new HashSet<>();
                        roles.add(role);
                        user.setRoles(roles);
                        return user;
                    })
            )
            .flatMap(user -> userRepository.save(user))
            .map(user -> authMapper.getRegisterResponse());
    }

    @Override
    public Mono<Void> validateToken() {
        return Mono.empty();
    }
}
