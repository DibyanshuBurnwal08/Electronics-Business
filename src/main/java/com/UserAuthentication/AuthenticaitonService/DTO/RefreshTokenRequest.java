package com.UserAuthentication.AuthenticaitonService.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequest {

    private String refreshToken;

}
