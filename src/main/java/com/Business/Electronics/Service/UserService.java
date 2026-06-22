package com.Business.Electronics.Service;

import com.Business.Electronics.DTO.AuthDTO;
import com.Business.Electronics.DTO.LoginDTO;
import com.Business.Electronics.DTO.UserDTO;
import com.Business.Electronics.Entity.Role;
import com.Business.Electronics.Entity.UserEntity;
import com.Business.Electronics.Repository.RegisterRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final RegisterRepo registerRepo;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final MailService mailService;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String adminMail;

    @Value("${spring.admin.password}")
    private String password;

    public UserDTO register(UserDTO dto) {
        UserEntity entity = toEntity(dto);
        if(registerRepo.findByEmail(adminMail).isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setEmail(adminMail);
            admin.setName("Dipu");
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            registerRepo.save(admin);
        }
        Optional<UserEntity> byEmail = registerRepo.findByEmail(entity.getEmail());
        if(byEmail.isEmpty()) {
            String verificationToken = UUID.randomUUID().toString();
            entity.setActivationToken(verificationToken);
            registerRepo.save(entity);
            String activationLink = "http://localhost:8080/activate?token=" + entity.getActivationToken();
            String subject = "Activate Your Account";
            String object = "Click this Link to activate your account:"+activationLink;
            mailService.sendEmail(entity.getEmail(), subject, object);
            return toDTO(entity);
        }
        return null;
    }

    public AuthDTO login(LoginDTO user) {
        Optional<UserEntity> byEmail = registerRepo.findByEmail(user.getEmail());
        if (byEmail.isEmpty()) {
            return null;
        }
        UserEntity entity = byEmail.get();
        if(!entity.isEnabled()) return null;
        String token = refreshTokenService.createRefreshToken(entity.getEmail()).getToken();
        if(passwordEncoder.matches(user.getPassword(), entity.getPassword())) {
            return AuthDTO.builder()
                    .email(entity.getEmail())
                    .accessToken(jwtService.generateToken(entity.getEmail()))
                    .refreshToken(token)
                    .build();
        }
        return null;
    }

    public boolean activateProfile(String token) {
        Optional<UserEntity> byActivationToken = registerRepo.findByActivationToken(token);
        return byActivationToken.map(user ->{
            user.setEnabled(true);
            user.setActivationToken(null);
            registerRepo.save(user);
            return true;
        }
        ).orElse(false);
    }

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    public List<UserDTO> getAllUser() {
        List<UserEntity> allUser = registerRepo.getAllUser();
        return allUser.stream().map(this::toDTO).toList();
    }

    private UserEntity toEntity(UserDTO dto) {
        return UserEntity
                .builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .createdAt(dto.getCreatedAt())
                .role(Role.USER)
                .enabled(false)
                .build();
    }

    private UserDTO toDTO(UserEntity entity) {
        return UserDTO
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .build();
    }


    public boolean makeAdmin(String email) {
        Optional<UserEntity> byEmail = registerRepo.findByEmail(email);
        if(byEmail.isPresent()) {
            UserEntity user = byEmail.get();
            user.setRole(Role.ADMIN);
            registerRepo.save(user);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String email) {
        Optional<UserEntity> byEmail = registerRepo.findByEmail(email);
        if(byEmail.isPresent()) {
            UserEntity user = byEmail.get();
            if(user.getRole().name().equals("ADMIN")) return false;
            registerRepo.delete(user);
            return true;
        }
        return false;
    }

    public void blockUser(String email) {
        UserEntity user = registerRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        if(user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Cannot Block Admin");
        }
        user.setEnabled(false);
        registerRepo.save(user);
    }

    public void unBlockUser(String email) {
        UserEntity user = registerRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        if(user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Cannot Un-Block Admin");
        }
        user.setEnabled(true);
        registerRepo.save(user);
    }

    public boolean isActivated(LoginDTO user) {
        return registerRepo.findByEmail(user.getEmail())
                .map(UserEntity::isEnabled)
                .orElse(false);
    }

}
