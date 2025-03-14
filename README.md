# Auth Microservice

Spring Boot Webflux microservice that handles auth operations (login, register).

## Stack
- Java 11
- Spring Boot 2.x
- Spring Security (JWT)
- Spring Webflux
- Spring Cloud Config Client
- Reactive Mongodb
- Openapi contract first
- Swagger ui

## Configuration
Service connects to Config Server using:
```properties
spring.application.name=ms-auth-service
spring.config.import=optional:configserver:http://localhost:8888
```
for properties
```yaml
eureka:
  instance:
    hostname: localhost
    instance-id: ${spring.application.name}:${random.int}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: ms-bootcamp-arcelles

server:
  port: ${PORT:0}

management:
  endpoints:
    web:
      exposure:
        include: health,circuitbreakerevents
  endpoint:
    health:
      show-details: always

application:
  config:
    jwt:
      secret: 56652850831004069536350134778564201658246816143034576D5A71347437
      expiration: 10
```

## Swagger
http://localhost:8091/swagger-ui.html

![ms-auth-service-2025-03-14-154517](https://github.com/user-attachments/assets/48907641-494d-45d9-abef-e29791e3d2ad)
