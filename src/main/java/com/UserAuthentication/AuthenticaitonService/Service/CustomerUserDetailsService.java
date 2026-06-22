package com.UserAuthentication.AuthenticaitonService.Service;

import com.UserAuthentication.AuthenticaitonService.Entity.UserEntity;
import com.UserAuthentication.AuthenticaitonService.Repository.RegisterRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final RegisterRepo registerRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = registerRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email Not Found"));
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_"+user.getRole().name())
                .build();
    }
}
