package com.Business.Electronics.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDTO {

    private String email;
    private String password;
    private String accessToken;
    private String refreshToken;

}
