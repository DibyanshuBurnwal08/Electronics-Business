package com.Business.Electronics.Controller;

import com.Business.Electronics.DTO.AuthDTO;
import com.Business.Electronics.DTO.LoginDTO;
import com.Business.Electronics.DTO.UserDTO;
import com.Business.Electronics.Service.RefreshTokenService;
import com.Business.Electronics.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        UserDTO register = userService.register(dto);
        if(register != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(register);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email Already Exits");
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {
        boolean isActive = userService.activateProfile(token);
        if(isActive) return ResponseEntity.ok("Account Activated");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token is used or activation token not found");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO user, HttpServletResponse response) {
        boolean isActivated = userService.isActivated(user);
        if(!isActivated) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Not activated");
        AuthDTO login = userService.login(user, response);
        if(login == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email Not Register / User is Blocked");
        }
        return ResponseEntity.status(HttpStatus.OK).body(login);
    }

    @PostMapping("user/logout")
    public ResponseEntity<String> logout(@CookieValue("refreshToken") String refreshToken) {
        refreshTokenService.logout(refreshToken);
        return ResponseEntity.ok("LogOut Successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        String user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

}
