package com.Business.Electronics.Service;

import com.Business.Electronics.Entity.RefreshToken;
import com.Business.Electronics.Entity.UserEntity;
import com.Business.Electronics.Repository.RefreshTokenRepo;
import com.Business.Electronics.exception.RefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDuration;

    private final RefreshTokenRepo tokenRepo;
    private final JwtService jwtService;

    public RefreshToken createRefreshToken(UserEntity entity) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(entity);
        refreshToken.setExpirationDate(Instant.now().plusMillis(refreshTokenDuration));
        return tokenRepo.save(refreshToken);
    }

    public RefreshToken verifyExpiry(RefreshToken token) {
        if(token.getExpirationDate().compareTo(Instant.now()) < 0) {
            tokenRepo.delete(token);
            throw new RefreshTokenException("Refresh Token Expired");
        }
        return token;
    }

    public Map<Object, Object> regenerateAccessToken (String refreshToken) {
        RefreshToken byToken = tokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new UsernameNotFoundException("Refresh Token Not exits"));
        RefreshToken refreshTokenEntity = verifyExpiry(byToken);
        UserEntity user = refreshTokenEntity.getUser();
        String accessToken = jwtService.generateToken(user.getEmail());
        return Map.of("accessToken", accessToken);
    }

    public void logout(String refreshToken) {
        RefreshToken byToken = tokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new UsernameNotFoundException("Unable To find Refresh Token"));
        tokenRepo.delete(byToken);
    }

    public void delete(RefreshToken refreshToken1) {
        tokenRepo.delete(refreshToken1);
    }
}
