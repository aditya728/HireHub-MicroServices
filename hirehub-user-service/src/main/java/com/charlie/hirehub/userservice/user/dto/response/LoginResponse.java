package com.charlie.hirehub.userservice.user.dto.response;

import com.charlie.hirehub.userservice.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String email;
    private Role role;
    private String message;
}
