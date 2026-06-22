package com.Business.Electronics.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDTO {

    private String email;
    private String password;
    private String accessToken;
    private String refreshToken;

}
