package io.backend.blogproject.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException exception) {
        return ResponseEntity.status(exception.getErrorCode().getCode())
                .body(Map.of("message", exception.getMessage()));
    }
}
