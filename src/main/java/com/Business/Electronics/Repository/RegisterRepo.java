package com.Business.Electronics.Repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.Business.Electronics.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByActivationToken(String token);

    @Query("SELECT u FROM UserEntity u WHERE u.role = 'USER'")
    List<UserEntity> getAllUser();

    @Query("SELECT COUNT(u) FROM UserEntity u where u.role = 'USER'")
    Integer totalUser();

}
