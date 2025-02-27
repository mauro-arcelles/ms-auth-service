package com.project1.ms_auth_service.business.mapper;

import com.project1.ms_auth_service.model.AuthResponse;
import com.project1.ms_auth_service.model.RegisterRequest;
import com.project1.ms_auth_service.model.RegisterResponse;
import com.project1.ms_auth_service.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User getRegisterUserEntity(RegisterRequest request) {
        return User.builder()
            .username(request.getUsername())
            .enabled(true)
            .build();
    }

    public AuthResponse getAuthResponse(String jwt) {
        AuthResponse response = new AuthResponse();
        response.setAccessToken(jwt);
        return response;
    }

    public RegisterResponse getRegisterResponse() {
        RegisterResponse response = new RegisterResponse();
        response.setMessage("User registered successfully");
        return response;
    }
}
