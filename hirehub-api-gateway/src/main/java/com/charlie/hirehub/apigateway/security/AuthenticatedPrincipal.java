package com.charlie.hirehub.apigateway.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedPrincipal {

    private Long userId;
    private String email;
    private Role role;
}
