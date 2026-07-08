package com.Business.Electronics.Config;

import com.Business.Electronics.Entity.AuthProvider;
import com.Business.Electronics.Entity.RefreshToken;
import com.Business.Electronics.Entity.Role;
import com.Business.Electronics.Entity.UserEntity;
import com.Business.Electronics.Repository.RegisterRepo;
import com.Business.Electronics.Service.JwtService;
import com.Business.Electronics.Service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final RegisterRepo registerRepo;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        assert user != null;
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        RefreshToken refreshToken = null;
        String role = null;
        Optional<UserEntity> byEmail = registerRepo.findByEmail(email);
        if(byEmail.isEmpty()) {
            UserEntity newUser = new UserEntity();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setProvider(AuthProvider.GOOGLE);
            newUser.setEnabled(true);
            newUser.setRole(Role.USER);
            role = newUser.getRole().toString();
            newUser = registerRepo.save(newUser);
            refreshToken = refreshTokenService.createRefreshToken(newUser);
        }
        String jwt = jwtService.generateToken(email);
        if(byEmail.isPresent()) {
            role = byEmail.get().getRole().toString();
            refreshToken = refreshTokenService.createRefreshToken(byEmail.get());
        }

        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(240000);
        response.addCookie(cookie);

        assert name != null;
        assert role != null;
        response.sendRedirect(
                "http://localhost:5173/oauth-success?token=" + jwt +
                        "&userName=" + URLEncoder.encode(name, StandardCharsets.UTF_8) +
                        "&role="+ URLEncoder.encode(role, StandardCharsets.UTF_8));
    }
}
