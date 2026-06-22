package com.Business.Electronics.Controller;

import com.Business.Electronics.DTO.ForgotPasswordRequest;
import com.Business.Electronics.DTO.ResetPasswordRequest;
import com.Business.Electronics.Service.ResetPasswordService;
import com.Business.Electronics.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;
    private final UserService userService;

    @PostMapping("forgot-password")
    public ResponseEntity<ForgotPasswordRequest> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        ForgotPasswordRequest getRequest = resetPasswordService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(getRequest);
    }

    @GetMapping("reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token) {
        boolean isValid = resetPasswordService.validateToken(token);
        if(isValid) return ResponseEntity.ok("Token is Valid");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("token is expired");
    }

    @PostMapping("reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        resetPasswordService.resetPassword(request.getToken(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body("Password Updated Successfully");
    }


}
