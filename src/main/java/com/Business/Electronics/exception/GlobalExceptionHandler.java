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

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<String> handleRefreshTokenException(RefreshTokenException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Refresh Token Expired");
    }

    @ExceptionHandler(AddProductFailedException.class)
    public ResponseEntity<String> handleAddProductException(AddProductFailedException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(CloudinaryException.class)
    public ResponseEntity<String> handleCloudinaryException(CloudinaryException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

}
