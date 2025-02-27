package com.project1.ms_auth_service.repository;

import com.project1.ms_auth_service.model.entity.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoleRepository extends ReactiveMongoRepository<Role, String> {
    Mono<Role> findByName(String name);
}
