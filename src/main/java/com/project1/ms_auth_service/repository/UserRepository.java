package com.project1.ms_auth_service.repository;

import com.project1.ms_auth_service.model.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<Boolean> existsByUsername(String email);

    Mono<User> findByUsername(String username);
}
