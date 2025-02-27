package com.project1.ms_auth_service.exception;

import com.project1.ms_auth_service.model.ResponseBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, List<String>>>> handleValidationErrors(WebExchangeBindException ex) {
        log.error("error", ex);
        Map<String, List<String>> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.groupingBy(
                FieldError::getField,
                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
            ));
        return Mono.just(ResponseEntity.badRequest().body(errors));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGenericError(Exception ex) {
        log.error("error", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<ResponseBase>> handleBadRequestException(Exception ex) {
        log.error("error", ex);
        ResponseBase responseBase = new ResponseBase();
        responseBase.setMessage(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(responseBase));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public Mono<ResponseEntity<ResponseBase>> handleInternalServerErrorException(Exception ex) {
        log.error("Error", ex);
        ResponseBase responseBase = new ResponseBase();
        responseBase.setMessage(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(responseBase));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Mono<ResponseEntity<ResponseBase>> handleUnauthorizedException(Exception ex) {
        log.error("Error", ex);
        ResponseBase responseBase = new ResponseBase();
        responseBase.setMessage(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(responseBase));
    }

}
