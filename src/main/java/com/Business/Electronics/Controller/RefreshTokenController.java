package com.Business.Electronics.Controller;

import com.Business.Electronics.Service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> getRefreshToken(@CookieValue("refreshToken") String refreshToken) {
        Map<Object, Object> accessToken = refreshTokenService.regenerateAccessToken(refreshToken);
        return ResponseEntity.ok(accessToken);
    }

}
