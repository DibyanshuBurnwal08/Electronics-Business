package com.UserAuthentication.AuthenticaitonService.Service;

import com.UserAuthentication.AuthenticaitonService.DTO.JwtResponse;
import com.UserAuthentication.AuthenticaitonService.DTO.RefreshTokenRequest;
import com.UserAuthentication.AuthenticaitonService.Entity.RefreshToken;
import com.UserAuthentication.AuthenticaitonService.Entity.UserEntity;
import com.UserAuthentication.AuthenticaitonService.Repository.RefreshTokenRepo;
import com.UserAuthentication.AuthenticaitonService.Repository.RegisterRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDuration;

    private final RefreshTokenRepo tokenRepo;
    private final RegisterRepo registerRepo;
    private final JwtService jwtService;

    public RefreshToken createRefreshToken(String email) {
        UserEntity user = registerRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        Optional<RefreshToken> byUserId = tokenRepo.findByUserId(user.getId());
        if(byUserId.isPresent()) {
            return byUserId.get();
        }
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpirationDate(Instant.now().plusMillis(refreshTokenDuration));
        return tokenRepo.save(refreshToken);
    }

    public RefreshToken verifyExpiry(RefreshToken token) {
        if(token.getExpirationDate().compareTo(Instant.now()) < 0) {
            tokenRepo.delete(token);
            throw new RuntimeException("Refresh Token Expired");
        }
        return token;
    }

    public JwtResponse getRefreshToken(RefreshTokenRequest tokenRequest) {
        RefreshToken byToken = tokenRepo.findByToken(tokenRequest.getRefreshToken())
                .orElseThrow(() -> new UsernameNotFoundException("Refresh Token Not exits"));
        RefreshToken refreshToken = verifyExpiry(byToken);
        UserEntity user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user.getEmail());
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
