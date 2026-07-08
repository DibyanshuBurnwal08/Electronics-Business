package com.Business.Electronics.Service;

import com.Business.Electronics.DTO.ForgotPasswordRequest;
import com.Business.Electronics.Entity.ResetPasswordEntity;
import com.Business.Electronics.Entity.UserEntity;
import com.Business.Electronics.Repository.RegisterRepo;
import com.Business.Electronics.Repository.ResetPasswordRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final RegisterRepo registerRepo;
    private final ResetPasswordRepo resetPasswordRepo;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public void resetPassword(String token, String password) {
        ResetPasswordEntity entity = resetPasswordRepo.findByResetToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Token is Expired"));
        if(validateToken(entity.getResetToken())) {
            UserEntity user = entity.getUser();
            user.setPassword(passwordEncoder.encode(password));
            registerRepo.save(user);
            resetPasswordRepo.delete(entity);
        }
    }

    public boolean validateToken(String token) {
        ResetPasswordEntity byResetToken = resetPasswordRepo.findByResetToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Token is Expired"));
        return byResetToken.getExpirationDate().isAfter(Instant.now());
    }

    public ForgotPasswordRequest forgotPassword(String email) {
        UserEntity user = registerRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        ResetPasswordEntity reset = new ResetPasswordEntity();
        String token = UUID.randomUUID().toString();
        reset.setResetToken(token);
        int expirationDate = 900000;
        reset.setExpirationDate(Instant.now().plusMillis(expirationDate));
        reset.setUser(user);
        resetPasswordRepo.save(reset);
        String link = "http://localhost:8080/reset-password?token="+reset.getResetToken();
        String subject = "Reset Your Password";
        String object = "Click this link to reset password for KULDEEP TELECOM:"+link;
        mailService.sendEmail(email, subject, object);
        return ForgotPasswordRequest.builder()
                .token(token)
                .email(email)
                .build();
    }

}
