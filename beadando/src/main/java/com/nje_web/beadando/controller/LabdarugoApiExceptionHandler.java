package com.nje_web.beadando.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice(assignableTypes = LabdarugoApiController.class)
public class LabdarugoApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiHiba> ervenytelenAdat(MethodArgumentNotValidException exception) {
        List<String> hibak = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(new ApiHiba("A kérés adatai hibásak.", hibak));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiHiba> apiHiba(ResponseStatusException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        return ResponseEntity.status(status).body(new ApiHiba(exception.getReason(), List.of()));
    }
}
