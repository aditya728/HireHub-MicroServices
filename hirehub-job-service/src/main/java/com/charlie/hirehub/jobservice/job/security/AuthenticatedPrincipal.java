package com.charlie.hirehub.jobservice.job.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@AllArgsConstructor
public class AuthenticatedPrincipal {

    private final Long userId;
    private final String email;
    private final String role;

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + role);
    }
}
