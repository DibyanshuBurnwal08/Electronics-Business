package com.Business.Electronics.exception;

import com.Business.Electronics.Service.RefreshTokenService;
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

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<String> handleRefreshTokenException(RefreshTokenException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Refresh Token Expired");
    }

}
