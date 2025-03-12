package com.project1.ms_auth_service.config;

import com.project1.ms_auth_service.config.auth.TokenAuthenticationConverter;
import com.project1.ms_auth_service.config.auth.UnauthorizedAuthenticationEntryPoint;
import com.project1.ms_auth_service.config.auth.jwt.JWTHeadersExchangeMatcher;
import com.project1.ms_auth_service.config.auth.jwt.JWTReactiveAuthenticationManager;
import com.project1.ms_auth_service.model.entity.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Autowired
    private TokenAuthenticationConverter tokenAuthenticationConverter;

    @Autowired
    private JWTReactiveAuthenticationManager jwtReactiveAuthenticationManager;

    private static final String[] AUTH_WHITELIST = {
        "/api/v1/auth/login",
        "/api/v1/auth/register"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, UnauthorizedAuthenticationEntryPoint entryPoint) {

        http.httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .logout().disable();

        http
            .exceptionHandling()
            .authenticationEntryPoint(entryPoint)
            .and()
            .authorizeExchange()
            .matchers(EndpointRequest.to("health", "info"))
            .permitAll()
            .and()
            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS)
            .permitAll()
            .and()
            .authorizeExchange()
            .matchers(EndpointRequest.toAnyEndpoint())
            .hasAuthority(RoleEnum.ROLE_ADMIN.toString())
            .and()
            .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
            .authorizeExchange()
            .pathMatchers(AUTH_WHITELIST).permitAll()
            .anyExchange().authenticated();

        return http.build();
    }

    @Bean
    public AuthenticationWebFilter webFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(jwtReactiveAuthenticationManager);
        authenticationWebFilter.setAuthenticationConverter(tokenAuthenticationConverter);
        authenticationWebFilter.setRequiresAuthenticationMatcher(new JWTHeadersExchangeMatcher());
        authenticationWebFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        return authenticationWebFilter;
    }


}
