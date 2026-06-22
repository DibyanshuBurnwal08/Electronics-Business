package com.UserAuthentication.AuthenticaitonService.Controller;

import com.UserAuthentication.AuthenticaitonService.DTO.JwtResponse;
import com.UserAuthentication.AuthenticaitonService.DTO.RefreshTokenRequest;
import com.UserAuthentication.AuthenticaitonService.Service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> getRefreshToken(@RequestBody RefreshTokenRequest tokenRequest) {
        JwtResponse response = refreshTokenService.getRefreshToken(tokenRequest);
        return ResponseEntity.ok(response);
    }

}
