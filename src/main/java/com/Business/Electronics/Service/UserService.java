package com.Business.Electronics.Service;

import com.Business.Electronics.DTO.AuthDTO;
import com.Business.Electronics.DTO.LoginDTO;
import com.Business.Electronics.DTO.UserDTO;
import com.Business.Electronics.Entity.AuthProvider;
import com.Business.Electronics.Entity.RefreshToken;
import com.Business.Electronics.Entity.Role;
import com.Business.Electronics.Entity.UserEntity;
import com.Business.Electronics.Repository.RegisterRepo;
import com.Business.Electronics.exception.OAuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
            admin.setProvider(AuthProvider.LOCAL);
            registerRepo.save(admin);
        }
        Optional<UserEntity> byEmail = registerRepo.findByEmail(entity.getEmail());
        if(byEmail.isEmpty()) {
            String verificationToken = UUID.randomUUID().toString();
            entity.setActivationToken(verificationToken);
            registerRepo.save(entity);
            String activationLink = "http://localhost:8080/activate?token=" + entity.getActivationToken();
            String subject = "Activate Your Account";
            String object = "Click this Link to activate your account on KULDEEP TELECOM:"+activationLink;
            mailService.sendEmail(entity.getEmail(), subject, object);
            return toDTO(entity);
        }
        return null;
    }

    public AuthDTO login(LoginDTO user, HttpServletResponse response) {

        Optional<UserEntity> byEmail = registerRepo.findByEmail(user.getEmail());
        if (byEmail.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }

        UserEntity entity = byEmail.get();

        if(entity.getProvider().equals(AuthProvider.GOOGLE)) {
            throw new OAuthException("This account uses Google Sign-In. Please continue with Google");
        }

        RefreshToken refreshToken1 = refreshTokenService.createRefreshToken(entity);
        String token = refreshToken1.getToken();

        if(passwordEncoder.matches(user.getPassword(), entity.getPassword())) {

            Cookie cookie = new Cookie("refreshToken", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setMaxAge(240000);
            response.addCookie(cookie);

            return AuthDTO.builder()
                    .name(entity.getName())
                    .email(entity.getEmail())
                    .role(entity.getRole().name())
                    .accessToken(jwtService.generateToken(entity.getEmail()))
                    .build();
        } else {
            refreshTokenService.delete(refreshToken1);
        }
        return null;
    }

    public boolean activateProfile(String token) {
        Optional<UserEntity> byActivationToken = registerRepo.findByActivationToken(token);
        return byActivationToken.map(user ->{
            user.setEnabled(true);
            user.setProvider(AuthProvider.LOCAL);
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
                .enabled(entity.isEnabled())
                .build();
    }


    public boolean makeAdmin(Long id) {
        Optional<UserEntity> byId = registerRepo.findById(id);
        if(byId.isPresent()) {
            UserEntity user = byId.get();
            user.setRole(Role.ADMIN);
            registerRepo.save(user);
            return true;
        }
        return false;
    }

    public boolean deleteUser(Long id) {
        Optional<UserEntity> byId = registerRepo.findById(id);
        if(byId.isPresent()) {
            UserEntity user = byId.get();
            if(user.getRole().name().equals("ADMIN")) return false;
            registerRepo.delete(user);
            return true;
        }
        return false;
    }

    public void blockUser(Long id) {
        UserEntity user = registerRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        if(user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Cannot Block Admin");
        }
        user.setEnabled(false);
        registerRepo.save(user);
    }

    public void unBlockUser(Long id) {
        UserEntity user = registerRepo.findById(id)
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

    public Integer totalUsers() {
        return registerRepo.totalUser();
    }

    public List<UserDTO> getAllAdmins() {
        List<UserEntity> allAdmins = registerRepo.getAllAdmins();
        return allAdmins.stream().map(this::toDTO).toList();
    }
}
