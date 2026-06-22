package com.Business.Electronics.Service;

import com.Business.Electronics.Entity.UserEntity;
import com.Business.Electronics.Repository.RegisterRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
