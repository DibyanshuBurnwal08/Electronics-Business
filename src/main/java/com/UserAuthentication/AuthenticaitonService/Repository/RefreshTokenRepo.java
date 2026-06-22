package com.UserAuthentication.AuthenticaitonService.Repository;

import com.UserAuthentication.AuthenticaitonService.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {

        Optional<RefreshToken> findByToken(String token);

        Optional<RefreshToken> findByUserId(Long userId);

}
