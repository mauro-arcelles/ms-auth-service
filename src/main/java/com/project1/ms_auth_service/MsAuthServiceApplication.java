package com.project1.ms_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsAuthServiceApplication.class, args);
    }

}
