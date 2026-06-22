package com.Business.Electronics.Repository;

import com.Business.Electronics.Entity.ResetPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepo extends JpaRepository<ResetPasswordEntity, Long> {

    Optional<ResetPasswordEntity> findByResetToken(String token);

}
