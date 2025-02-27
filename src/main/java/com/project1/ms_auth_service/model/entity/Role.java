package com.project1.ms_auth_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private String id;

    private RoleEnum name;
}
