package com.Business.Electronics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<String> handleOAuthException(OAuthException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Login with Google");
    }

}
