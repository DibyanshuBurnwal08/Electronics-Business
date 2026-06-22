package com.UserAuthentication.AuthenticaitonService.Repository;

import com.UserAuthentication.AuthenticaitonService.Entity.ResetPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepo extends JpaRepository<ResetPasswordEntity, Long> {

    Optional<ResetPasswordEntity> findByResetToken(String token);

}
