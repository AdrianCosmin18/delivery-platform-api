package com.example.deliveryapp.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DeliveryAppExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleExceptionNotFound(RuntimeException e) {
        return ResponseEntity.badRequest().body(e);
    }

}
